/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.VipConfigurer;
import fr.insalyon.creatis.vip.api.rest.security.SpringCompatibleUser;
import fr.insalyon.creatis.vip.api.business.ApiContext;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Used to create an ApiContext in REST requests
 *
 * Created by abonnet on 7/25/16.
 */
@Service
public class RestApiBusiness {

    private final static Logger logger = Logger.getLogger(RestApiBusiness.class);

    @Autowired
    private VipConfigurer vipConfigurer;
    @Autowired
    private ApiContext apiContext;

    public ApiContext getApiContext(HttpServletRequest request, boolean isAuthenticated) {
        User vipUser = null;
        if (isAuthenticated) {
            // if the user is authenticated, fetch it in the request info
            Authentication authentication = (Authentication) request.getUserPrincipal();
            SpringCompatibleUser springCompatibleUser =
                    (SpringCompatibleUser) authentication.getPrincipal();
            vipUser = springCompatibleUser.getVipUser();
        }
        // configure VIP if it has not been done today
        vipConfigurer.configureIfNecessary();
        if (apiContext != null) {
            apiContext.init(request, null, vipUser);
        }
        return new ApiContext(request, null, vipUser);
    }
}
