/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.dao.h2;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class UsersGroupsData implements UsersGroupsDAO {

    private final static Logger logger = Logger.getLogger(UsersGroupsData.class);
    private Connection connection;

    public UsersGroupsData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public void add(String email, String groupname, CoreConstants.ROLE role)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPUsersGroups(email, groupname, role) "
                    + "VALUES(?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, groupname);
            ps.setString(3, role.name());
            ps.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param email
     * @return
     * @throws DAOException 
     */
    public Map<String, CoreConstants.ROLE> getUserGroups(String email)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "groupname, role "
                    + "FROM VIPUsersGroups "
                    + "WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            Map<String, CoreConstants.ROLE> groups = new HashMap<String, CoreConstants.ROLE>();

            while (rs.next()) {
                groups.put(rs.getString("groupname"),
                        CoreConstants.ROLE.valueOf(rs.getString("role")));
            }

            return groups;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param groups
     * @throws DAOException 
     */
    public void setUserGroups(String email, Map<String, CoreConstants.ROLE> groups)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPUsersGroups "
                    + "WHERE email=?");

            ps.setString(1, email);
            ps.execute();

            for (String group : groups.keySet()) {
                add(email, group, groups.get(group));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param groups
     * @return
     * @throws DAOException
     */
    public List<String> getUsersFromGroups(List<String> groups) throws DAOException {

        try {
            StringBuilder sb = new StringBuilder();

            for (String groupName : groups) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append("groupname = '" + groupName + "'");
            }
            PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT "
                    + "first_name, last_name "
                    + "FROM VIPUsers vu, VIPUsersGroups vg "
                    + "WHERE vu.email = vg.email AND (" + sb.toString() + ")");
            
            ResultSet rs = ps.executeQuery();
            List<String> users = new ArrayList<String>();
            
            while (rs.next()) {
                users.add(rs.getString("first_name") + " "
                        + rs.getString("last_name"));
            }
            
            return users;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}