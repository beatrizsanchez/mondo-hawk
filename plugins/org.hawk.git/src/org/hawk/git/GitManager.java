package org.hawk.git;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.hawk.core.IConsole;
import org.hawk.core.ICredentialsStore;
import org.hawk.core.IModelIndexer;
import org.hawk.core.IVcsManager;
import org.hawk.core.VcsCommit;
import org.hawk.core.VcsCommitItem;
import org.hawk.core.VcsRepositoryDelta;

public class GitManager implements IVcsManager {

	private IConsole console;
	private boolean isActive = true;

	private String repositoryURL;
	private String username;
	private String password;
	private IModelIndexer indexer;
	private boolean isFrozen = false;

	private Git git;
	private Repository gitRepository;
	private String branch;

	public static void main(String[] args) {
		GitManager gitManager = new GitManager();
		try {
			gitManager.init("/Users/bea/gitTests/new_repo", null);
			/*String current = gitManager.getCurrentRevision();
			String first = gitManager.getFirstRevision();
			System.out.println(current);
			System.out.println(first);
			gitManager.getDelta(first, current);
			*/
			
			
			/*
			Repository repository = gitManager.getGitRepository();
			
			Iterator<RevCommit> iterator = gitManager.getCommits().iterator();
			RevCommit currentCommit = iterator.next();
			RevCommit prevCommit = iterator.next();
			ObjectReader reader = repository.newObjectReader();
			CanonicalTreeParser treeIter = new CanonicalTreeParser();
			//ObjectId tree = repository.resolve(thisCommit.name());
			try (RevWalk rw = new RevWalk(repository)) {
				RevTree thisTree = rw.parseTree(currentCommit);
				treeIter.reset(reader, thisTree);
				rw.close();
			} 

			CanonicalTreeParser prevTreeIter = new CanonicalTreeParser();
			try (RevWalk rw = new RevWalk(repository)) {
				//ObjectId prevTree = repository.resolve(prevCommit.name());
				RevTree prevTree = rw.parseTree(prevCommit);
				prevTreeIter.reset(reader, prevTree);
				rw.close();
			} 	
				
			DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
			diffFormatter.setRepository(repository);
			List<DiffEntry> entries = diffFormatter.scan(prevTreeIter, treeIter);
			*/
			
			/*
			TreeWalk treeWalk = new TreeWalk(repository);
			try (RevWalk rw = new RevWalk(repository)) {
				RevTree thisTree = rw.parseTree(currentCommit);
				treeWalk.reset(thisTree);
				rw.close();
			}
			int count = 0;
			while (treeWalk.next()) {
				treeWalk.getPathString();
				count = count + 1;
			}
			
			treeWalk.close();*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCurrentRevision() throws Exception {
		return getCommits().iterator().next().getName();
	}

	@Override
	public String getFirstRevision() throws Exception {
		Iterator<RevCommit> commits = getCommits().iterator();
		RevCommit last = null;
		while (commits.hasNext()) {
			last = commits.next();
		}
		if (last != null) {
			return last.name();
		} else {
			throw new Exception("Get First Revision returned null value");
		}
	}

	protected Iterable<RevCommit> getCommits() throws GitAPIException, NoHeadException, MissingObjectException,
			IncorrectObjectTypeException, AmbiguousObjectException, IOException {
		return getGit().log().add(getGitRepository().resolve(branch)).call();
	}

	@Override
	public File importFile(String revision, String path, File optionalTemp) {
		try {
			RevWalk revWalk = new RevWalk(gitRepository);
			RevCommit commit = revWalk.parseCommit(ObjectId.fromString(revision));
			revWalk.close();

			try (TreeWalk treeWalk = TreeWalk.forPath(gitRepository, path, commit.getTree())) {
				ObjectId blobId = treeWalk.getObjectId(0);
				try (ObjectReader objectReader = gitRepository.newObjectReader()) {
					ObjectLoader objectLoader = objectReader.open(blobId);
					byte[] bytes = objectLoader.getBytes();
					System.out.println(new String(bytes, StandardCharsets.UTF_8));
					try (FileOutputStream fOS = new FileOutputStream(optionalTemp)) {
						fOS.write(bytes);
						return optionalTemp;
					} 
				}
			}
		} catch (Exception e) {
			console.printerrln(e);
			return null;
		}
	}

	@Override
	public VcsRepositoryDelta getDelta(String startRevision, String endRevision) throws Exception {
		final Repository gitRepository = getGitRepository();

		VcsRepositoryDelta delta = new VcsRepositoryDelta();
		delta.setManager(this);

		ObjectId to = gitRepository.resolve(endRevision);
		Iterator<RevCommit> commits = null;
		if (startRevision == null || startRevision.isEmpty()) {	
			startRevision = getFirstRevision();
		} 
		
		ObjectId from = gitRepository.resolve(startRevision);
		commits = getGit().log().addRange(from, to).call().iterator();

		RevCommit prevCommit = null;
		while (commits.hasNext()) {
			RevCommit gitCommit = commits.next();
			VcsCommit commit = new VcsCommit();

			commit.setAuthor(gitCommit.getAuthorIdent().getName());
			commit.setMessage(gitCommit.getShortMessage());
			commit.setRevision(gitCommit.getName());
			commit.setDelta(delta);
			commit.setJavaDate(gitCommit.getAuthorIdent().getWhen());
			delta.getCommits().add(commit);

			GitUtils.diff(gitRepository, gitCommit, prevCommit, commit);

			prevCommit = gitCommit;
		}

		return delta;
	}

	@Override
	public Collection<VcsCommitItem> getDelta(String startRevision) throws Exception {
		return getDelta(startRevision, getCurrentRevision()).getCompactedCommitItems();
	}

	@Override
	public void run() throws Exception {
		try {
			/*final ICredentialsStore credStore = indexer.getCredentialsStore();
			if (username != null) {
				setCredentials(username, password, credStore);
			} else {
				final Credentials credentials = credStore.get(repositoryURL);
				if (credentials != null) {
					this.username = credentials.getUsername();
					this.password = credentials.getPassword();
				} else {
					
					console.printerrln("No username/password recorded for the repository " + repositoryURL);
					this.username = "";
					this.password = "";
				}
			}*/

			getFirstRevision();

			isActive = true;
		} catch (Exception e) {
			console.printerrln("exception in gitManager run():");
			console.printerrln(e);
		}
	}

	@Override
	public void init(String vcsloc, IModelIndexer hawk) throws Exception {
		this.repositoryURL = vcsloc;
		this.indexer = hawk;
		this.console = this.indexer.getConsole();
	}

	@Override
	public void setCredentials(String username, String password, ICredentialsStore credStore) {
		/*if (username != null && password != null && repositoryURL != null
				&& (!username.equals(this.username) || !password.equals(this.password))) {
			try {
				credStore.put(repositoryURL, new Credentials(username, password));
			} catch (Exception e) {
				console.printerrln("Could not save new username/password");
				console.printerrln(e);
			}
		}*/
		this.username = username;
		this.password = password;
		this.gitRepository = null;
	}

	@Override
	public String getType() {
		return getClass().getName();
	}

	@Override
	public String getHumanReadableName() {
		return "Git Monitor";
	}

	@Override
	public String getRepositoryPath(String rawPath) {
		final String emfUriPrefix = getLocation().replaceFirst("file:///", "file:/");
		if (rawPath.startsWith(emfUriPrefix)) {
			return rawPath.substring(emfUriPrefix.length());
		}
		return rawPath;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void shutdown() {
		repositoryURL = null;
		console = null;
	}

	@Override
	public String getLocation() {
		return repositoryURL;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAuthSupported() {
		return true;
	}

	@Override
	public boolean isPathLocationAccepted() {
		return true;
	}

	@Override
	public boolean isURLLocationAccepted() {
		return true;
	}

	@Override
	public boolean isFrozen() {
		return isFrozen;
	}

	@Override
	public void setFrozen(boolean frozen) {
		isFrozen = frozen;
	}

	protected Git getGit() {
		if (git == null) {
			String path = (repositoryURL.startsWith("file:")) ? repositoryURL.substring("file:".length()) : repositoryURL;
			git = GitUtils.connectToGitInstance(path, username, password);
		}
		return git;
	}

	protected Repository getGitRepository() {
		if (gitRepository == null) {
			gitRepository = getGit().getRepository();
			try {
				branch = gitRepository.getBranch();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gitRepository;
	}

}
