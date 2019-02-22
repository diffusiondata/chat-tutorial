/*******************************************************************************
 * Copyright (C) 2019 Push Technology Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.pushtechnology.chattutorial.app;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.control.clients.ClientControl;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.datatype.json.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Implementation of RequestHandler for authenticating clients and granting
 * topic update privileges.
 *
 */
public final class SignInHandler implements MessagingControl.RequestHandler<JSON, JSON> {
    private static final Logger LOG = LoggerFactory.getLogger(SignInHandler.class);
    private final Session session;
    
    /**
     * A map containing the authentication information.
     */
    private final Map<String, String> userAuthenticationMap;

    /**
     * Constructor accepting a diffusion session used to change roles.
     */
    public SignInHandler(Session session) {
        this.session = session;
        userAuthenticationMap = new HashMap<String, String>(3);
        userAuthenticationMap.put("Push", "password2");
        userAuthenticationMap.put("Diffusion", "password3");
    }

    @Override
    public void onClose() {
        LOG.info("Closing Handler.");
    }

    @Override
    public void onError(ErrorReason errorReason) {
        LOG.error(errorReason.toString());
    }

    @Override
    public void onRequest(JSON request, RequestContext context, Responder<JSON> responder) {
        final Map<String, String> signinginUserMap;
        final String username;
        final String password;

        try {
            signinginUserMap = mapJson(request.asInputStream());
        } catch (Exception e) {
            LOG.error("Parsing json to Map failed.", e);
            responder.respond(Diffusion.dataTypes().json()
                    .fromJsonString("{ \"status\": \"Fail\", \"message\": \"Authentication Failed.\" }"));
            return;
        }

        username = signinginUserMap.get("username");
        password = signinginUserMap.get("password");

        if (username == null || password == null) {
            responder.respond(Diffusion.dataTypes().json()
                    .fromJsonString("{ \"status\": \"Fail\", \"message\": \"Authentication Failed.\" }"));
            return;
        }

        if (!userAuthenticationMap.containsKey(username)
                || !userAuthenticationMap.get(username).equals(password)) {
            responder.respond(Diffusion.dataTypes().json()
                    .fromJsonString("{ \"status\": \"Fail\", \"message\": \"Authentication Failed.\" }"));
        }

        // with ClientControl we can change the role for the Session.
        final ClientControl clientControl = session.feature(ClientControl.class);

        // Diffusion role that is going to be assigned to sessions who pass
        // authentication.
        clientControl
                .changeRoles(context.getSessionId(), new HashSet<String>(), new HashSet<String>(Arrays.asList("CHAT")))
                .thenAccept((ignored) -> {
                    responder.respond(Diffusion.dataTypes().json().fromJsonString("{ \"status\": \"OK\" }"));
                }).exceptionally((err) -> {
                    LOG.error("Changing Role failed.", err);
                    responder.respond(Diffusion.dataTypes().json().fromJsonString(
                            "{ \"status\": \"Fail\", \"message\": \"Server Error while changing roles.\" }"));
                    return null;
                });
    }

    /**
     * Mapping for the json object request.
     * 
     */
    private static Map<String, String> mapJson(InputStream stream) throws IOException {
        final CBORFactory factory = new CBORFactory();
        final ObjectMapper mapper = new ObjectMapper();
        final TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
        };

        final CBORParser parser = factory.createParser(stream);
        return mapper.readValue(parser, typeReference);
    }
}