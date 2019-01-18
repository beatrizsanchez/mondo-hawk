package org.hawk.git;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.hawk.core.VcsChangeType;
import org.hawk.core.VcsCommit;
import org.hawk.core.VcsCommitItem;

public class GitUtils {
	
	public static Git connectToGitInstance(String url) {
		return connectToGitInstance(url, null, null);
	}
	
	public static Git connectToGitInstance(String url, String username, String password) {
		
		Git repository = null;
		try {
			repository = GitUtils.setupRepository(url);
		} catch (Exception e) {
			System.err
					.println("error while creating an GitRepository for location '"
							+ url + "': " + e.getMessage());
		}
		
		if (username != null && !username.isEmpty() && password !=null) {
			new UsernamePasswordCredentialsProvider( username, password ) ;	
		}
		
		return repository;
	}
	
	public static Git setupRepository(String url)
			throws Exception {
		if (new File(url).exists()) {			
			return Git.open(new File(url));
		} else {
			return null; // FIXME Not yet supported
		}
	}	
	
	public static void diff(Repository repository, RevCommit thisCommit, RevCommit prevCommit, VcsCommit commit) throws Exception {
		ObjectReader reader = repository.newObjectReader();
		
		CanonicalTreeParser treeIter = new CanonicalTreeParser();
		try (RevWalk rw = new RevWalk(repository)) {
			RevTree thisTree = rw.parseTree(thisCommit);
			treeIter.reset(reader, thisTree);
			rw.close();
		} 

		if (prevCommit!= null) {			
			CanonicalTreeParser prevTreeIter = new CanonicalTreeParser();
			try (RevWalk rw = new RevWalk(repository)) {
				RevTree prevTree = rw.parseTree(prevCommit);
				prevTreeIter.reset(reader, prevTree);
				rw.close();
			} 
			
			DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
			diffFormatter.setRepository(repository);
			List<DiffEntry> entries = diffFormatter.scan(prevTreeIter, treeIter);
					
			for (DiffEntry entry : entries) {
				VcsCommitItem commitItem = new VcsCommitItem();
				commitItem.setCommit(commit);
				commit.getItems().add(commitItem);
				
				switch (entry.getChangeType()) {
				case ADD:
					commitItem.setChangeType(VcsChangeType.ADDED);
					commitItem.setPath(entry.getNewPath());
					break;
				case DELETE:
					commitItem.setChangeType(VcsChangeType.DELETED);
					commitItem.setPath(entry.getOldPath());
					break;
				case MODIFY:
					commitItem.setChangeType(VcsChangeType.UPDATED);
					commitItem.setPath(entry.getOldPath());
					break;
				case COPY:
					commitItem.setChangeType(VcsChangeType.ADDED);
					commitItem.setPath(entry.getNewPath());
					break;
				case RENAME:
					// Split in two commitItems
					commitItem.setChangeType(VcsChangeType.ADDED);
					commitItem.setPath(entry.getNewPath());
	
					VcsCommitItem commitItem2 = new VcsCommitItem();
					commitItem2.setCommit(commit);
					commitItem.setChangeType(VcsChangeType.DELETED);
					commitItem.setPath(entry.getOldPath());
					commit.getItems().add(commitItem2);
					break;
				default:
					System.err.println("something went wrong");
					commitItem.setChangeType(VcsChangeType.UNKNOWN);
					commitItem.setPath(entry.getNewPath());
				}
			}
			diffFormatter.close();
		} else {
			// Check if this is effectively what we want to do.
			TreeWalk treeWalk = new TreeWalk(repository);
			try (RevWalk rw = new RevWalk(repository)) {
				RevTree thisTree = rw.parseTree(thisCommit);
				treeWalk.reset(thisTree);
				rw.close();
			}
			while (treeWalk.next()) {
				VcsCommitItem commitItem = new VcsCommitItem();
				commitItem.setChangeType(VcsChangeType.ADDED);
				commitItem.setPath(treeWalk.getPathString());
				commitItem.setCommit(commit);
				commit.getItems().add(commitItem);
			}
			treeWalk.close();

		}
	}
}
