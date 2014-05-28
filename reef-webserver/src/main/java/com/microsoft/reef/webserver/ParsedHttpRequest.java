/**
 * Copyright (C) 2014 Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.reef.webserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Parsed HttpServletRequest
 */
public final class ParsedHttpRequest {

  private final HttpServletRequest request;
  private final String pathInfo;
  private final String method;
  private final String queryString;
  private final String requestUri;
  private final String requestUrl;
  private final String serveletPath;
  private final byte[] inputStream;
  private final String targetSpecification;
  private final String targetEntity;

  private final Map<String, String> headers = new HashMap();
  private final Map<String, List<String>> queryPairs = new LinkedHashMap<>();

  /**
   * parse HttpServletRequest
   *
   * @param request
   * @throws IOException
   * @throws ServletException
   */
  public ParsedHttpRequest(final HttpServletRequest request) throws IOException, ServletException {

    this.request = request;

    this.pathInfo = request.getPathInfo();
    this.method = request.getMethod();
    this.queryString = request.getQueryString();
    this.requestUri = request.getRequestURI();
    this.serveletPath = request.getServletPath();
    this.requestUrl = request.getRequestURL().toString();

    for (final Enumeration en = request.getHeaderNames(); en.hasMoreElements(); ) {
      final String headerName = en.nextElement().toString();
      this.headers.put(headerName, request.getHeader(headerName));
    }

    final int len = request.getContentLength();
    if (len > 0) {
      this.inputStream = new byte[len];
      request.getInputStream().read(this.inputStream);
    } else {
      this.inputStream = null;
    }

    if (this.requestUri != null) {
      final String[] parts = this.requestUri.split("/");
      this.targetSpecification = parts.length > 1 ? parts[1] : null;
      this.targetEntity = parts.length > 2 ? parts[2] : null;
    } else {
      this.targetSpecification = null;
      this.targetEntity = null;
    }

    if (this.queryString != null) {
      final String[] pairs = this.queryString.split("&");
      for (final String pair : pairs) {
        final int idx = pair.indexOf("=");
        if (idx != -1) {
          final String rKey = pair.substring(0, idx);
          final String rValue = pair.substring(idx + 1);
          final String key = URLDecoder.decode(rKey, "UTF-8");
          final String value = URLDecoder.decode(rValue, "UTF-8");
          List<String> valuesList = this.queryPairs.get(key);
          if (valuesList == null) {
            valuesList = new ArrayList<>(1);
            this.queryPairs.put(key, valuesList);
          }
          valuesList.add(value);
        }
      }
    }
  }

  /**
   * get target to match specification like "Reef"
   *
   * @return specification
   */
  public String getTargetSpecification() {
    return this.targetSpecification;
  }

  /**
   * get query string like "id=12345"
   *
   * @return
   */
  public String getQueryString() {
    return this.queryString;
  }

  /**
   * get target target entity like "Evaluators"
   *
   * @return
   */
  public String getTargetEntity() {
    return this.targetEntity;
  }

  /**
   * get http request method like "Get"
   *
   * @return
   */
  public String getMethod() {
    return this.method;
  }

  /**
   * get input Stream
   *
   * @return
   */
  public byte[] getInputStream() {
    return this.inputStream;
  }

  /**
   * get request headers
   *
   * @return
   */
  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public Map<String, List<String>> getQueryMap() {
    return this.queryPairs;
  }

  public String getRequestUrl() {
    return this.requestUrl;
  }
}
