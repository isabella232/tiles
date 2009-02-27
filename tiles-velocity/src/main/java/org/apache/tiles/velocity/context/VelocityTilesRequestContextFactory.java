/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.velocity.context;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.velocity.context.Context;

/**
 * 
 * @author anydoby
 * 
 * @since Mar 15, 2008
 */
public class VelocityTilesRequestContextFactory implements TilesRequestContextFactory, TilesRequestContextFactoryAware {

    /**
     * Parent Tiles context factory.
     */
    private TilesRequestContextFactory parent;

    public TilesRequestContext createRequestContext(TilesApplicationContext context, Object... requestItems) {
        if (requestItems.length == 4 && requestItems[0] instanceof Context
                && requestItems[1] instanceof HttpServletRequest
                && requestItems[2] instanceof HttpServletResponse
                && requestItems[3] instanceof Writer) {
            Context ctx = (Context) requestItems[0];
            HttpServletRequest request = (HttpServletRequest) requestItems[1];
            HttpServletResponse response = (HttpServletResponse) requestItems[2];
            Writer writer = (Writer) requestItems[3];
            TilesRequestContext enclosedRequest;
            if (parent != null) {
                enclosedRequest = parent.createRequestContext(context, request, response);
            } else {
                enclosedRequest = new ServletTilesRequestContext(context, request, response);
            }
            return new VelocityTilesRequestContext(enclosedRequest,
                    ctx, writer);
        } else if (requestItems.length == 1
            && requestItems[0] instanceof VelocityTilesRequestContext) {
            // FIXME is it necessary?
            
            VelocityTilesRequestContext ctx = (VelocityTilesRequestContext) requestItems[0];
            return ctx;
        }
        return null;
    }

    public void init(Map<String, String> configurationParameters) {
        // TODO Auto-generated method stub

    }

    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
        this.parent = contextFactory;
    }
}
