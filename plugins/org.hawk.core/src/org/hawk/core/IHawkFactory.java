/*******************************************************************************
 * Copyright (c) 2015 The University of York.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0
 *
 * Contributors:
 *     Antonio Garcia-Dominguez - initial API and implementation
 ******************************************************************************/
package org.hawk.core;

import java.io.File;
import java.util.List;

import org.hawk.core.IStateListener.HawkState;


/**
 * Interface for a factory of {@link IHawk} objects. Useful for having plugins
 * contribute different implementations of {@link IHawk} (which may introduce
 * their own dependencies).
 */
public interface IHawkFactory {

	/**
	 * Basic information about existing instances. This should only have
	 * the minimum information to be able to recreate an instance.
	 */
	public final static class InstanceInfo {
		public final String name, dbType;
		public final HawkState state;

		public InstanceInfo(String name, String dbType, HawkState state) {
			this.name = name;
			this.dbType = dbType;
			this.state = state;
		}
	}

	/**
	 * Creates a new instance of the desired implementation.
	 *
	 * @param name
	 *            Name of the Hawk instance to be created.
	 * @param storageFolder
	 *            Local storage folder for the Hawk instance.
	 * @param location
	 *            Additional location on top of the storage folder (e.g. a
	 *            Thrift API URL or the URL to a remote graph). This is only
	 *            necessary if {@link #instancesUseLocation()} returns
	 *            <code>true</code>.
	 * @param credStore
	 *            Credential storage to be used to persist VCS login details.
	 * @param console
	 *            {@link IConsole} implementation used to print messages.
	 * @param enabledPlugins
	 *            Plugins that should be enabled in the created instance.
	 */
	IHawk create(String name, File storageFolder, String location, ICredentialsStore credStore, IConsole console, List<String> enabledPlugins) throws Exception;

	/**
	 * Returns information from all the {@link IHawk} instances that
	 * already exist at a particular location. The location should be
	 * disregarded for implementations that return <code>false</code> in
	 * {@link #instancesUseLocation()}.
	 */
	InstanceInfo[] listInstances(String location) throws Exception;

	/**
	 * Returns a set of strings with the supported backends at that location,
	 * or <code>null</code> if we should know about them in some other way
	 * (e.g. using OSGi configuration elements).
	 */
	List<String> listBackends(String location) throws Exception;

	/**
	 * Returns a list of all the available plugins at that location, or
	 * <code>null</code> if we should know about them in some other way
	 * (e.g. using OSGi configuration elements).
	 */
	List<String> listPlugins(String location) throws Exception;

	/**
	 * Indicates whether the created instance should be customized with the
	 * contents of the extension points in the local installation (
	 * <code>true</code>) or not ( <code>false</code>).
	 */
	boolean instancesAreExtensible();

	/**
	 * Indicates whether instances require creating the graph (
	 * <code>true</code>), or not (<code>false</code>).
	 */
	boolean instancesCreateGraph();

	/**
	 * Indicates whether instances take a location in addition to the local
	 * storage folder (<code>true</code>) or not (<code>false</code>).
	 */
	boolean instancesUseLocation();
	
	/**
	 * Indicates whether the instance is remote (<code>true</code>) or not (<code>false</code>).
	 */
	boolean isRemote();
	
	String getHumanReadableName();

}
