/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2015-2017 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package omero.gateway;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Holds all necessary information needed for connecting to an OMERO server
 * 
 * @author Dominik Lindner &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:d.lindner@dundee.ac.uk">d.lindner@dundee.ac.uk</a>
 * @since 5.1
 */

public class LoginCredentials {

    /** The user */
    private UserCredentials user;

    /** The server information */
    private ServerInformation server;

    /** Name of the application which wants to connect to the server */
    private String applicationName;

    /** Flag to enable encryption */
    private boolean encryption = true;

    /** Flag to enable network checks */
    private boolean checkNetwork = true;

    /** Data compression level */
    private float compression = 0.85f;

    /** ID of the group if not the default group of the user should be used */
    private long groupID = -1;

    /** The connection argument.*/
    private ImmutableList<String> args;

    /** Whether to check the client-server versions */
    private boolean checkVersion = true;

    /** Default websocket ports (this might be moved into omero.constants
     * in future) **/
    enum DefaultPort {
        WS(80), WSS(443), HTTP(80), HTTPS(443);
        final int port;
        DefaultPort(int port) {
            this.port = port;
        }

        /**
         * Get the DefaultPort for the given protocol
         * @param protocol The protocol
         * @return The DefaultPort
         * @throws IllegalArgumentException If not found
         */
        static DefaultPort fromProtocol(String protocol) throws IllegalArgumentException {
            String msg = protocol+" is not supported. Supported protocols: ";
            for (DefaultPort p : DefaultPort.values()) {
                msg += p.name()+" ";
                if (p.name().equals(protocol.toUpperCase()))
                    return p;
            }
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Creates a new instance
     */
    public LoginCredentials() {
        user = new UserCredentials();
        server = new ServerInformation();
        args = null;
    }

    /**
     * Creates a new instance.
     * 
     * @param args
     *            The connection arguments. Note: When using this constructor
     *            the '#' character has to be escaped with a backslash!
     */
    public LoginCredentials(String[] args) {
        this();
        if (args == null) {
            throw new IllegalArgumentException("No connection arguments");
        }
        this.args = ImmutableList.copyOf(args);
    }

    /**
     * Creates a new instance with the given credentials and default port
     * 
     * @param username
     *            The username or alternatively a session ID (in which case
     *            the password will be ignored)
     * @param password
     *            The password
     * @param host
     *            The server hostname or websocket URL
     */
    public LoginCredentials(String username, String password, String host) {
        this(username, password, host, -1);
    }

    /**
     * Creates a new instance with the given credentials
     * 
     * @param username
     *            The username or alternatively a session ID (in which case
     *             the password will be ignored)
     * @param password
     *            The password
     * @param host
     *            The server hostname or websocket URL
     * @param port
     *            The server port
     */
    public LoginCredentials(String username, String password, String host,
            int port) {
        this();
        user.setUsername(username);
        user.setPassword(password);
        server.setHost(host);
        if (port >= 0) {
            server.setPort(port);
        }
        else if (server.getPort() < 0) {
            // set default ports
            if (!server.isURL()) {
                server.setPort(omero.constants.GLACIER2PORT.value);
            }
            else if (server.getPort() < 0) {
                server.setPort(DefaultPort.fromProtocol(server.getProtocol()).port);
            }
        }
    }

    /**
     * Returns the arguments if set as a read-only list.
     *
     * @return See above.
     */
    public List<String> getArguments() { return args; }

    /**
     * @return If encryption is enabled
     */
    public boolean isEncryption() {
        return encryption;
    }

    /**
     * Enable/Disable encryption
     * 
     * @param encryption
     *            See above
     */
    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    /**
     * @return If network checks should be performed
     */
    public boolean isCheckNetwork() {
        return checkNetwork;
    }

    /**
     * Enable/Disable network checks
     * 
     * @param checkNetwork
     *            See above
     */
    public void setCheckNetwork(boolean checkNetwork) {
        this.checkNetwork = checkNetwork;
    }

    /**
     * Returns the compression level.
     * @return The compression level
     */
    public float getCompression() {
        return compression;
    }

    /**
     * Sets the compression level
     * 
     * @param compression
     *            See above
     */
    public void setCompression(float compression) {
        this.compression = compression;
    }

    /**
     * @return The application name
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Set the application name
     * 
     * @param applicationName
     *            See above
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Returns the credentials.
     * @return The {@link UserCredentials}
     */
    public UserCredentials getUser() {
        return user;
    }

    /**
     * Returns the server information.
     * @return The {@link ServerInformation}
     */
    public ServerInformation getServer() {
        return server;
    }

    /**
     * Returns the OMERO group identifier.
     * @return The groupID to use for the connection
     */
    public long getGroupID() {
        return groupID;
    }

    /**
     * Sets the groupID to use for the connection
     * 
     * @param groupID
     *            The group id
     */
    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    /**
     * Returns whether the version check is enabled
     * @return whether the version check is enabled
     */
    public boolean getCheckVersion() {
        return this.checkVersion;
    }

    /**
     * Enable/Disable version check
     *
     * @param checkVersion Whether to check the client and server versions are compatible
     */
    public void setCheckVersion(boolean checkVersion) {
        this.checkVersion = checkVersion;
    }

}
