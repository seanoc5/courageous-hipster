package com.oconeco.courageous.service;

import java.io.IOException;

public interface WebPageDownloadWebClientService {
    String downloadWithWebClient(String uri) throws IOException;
}
