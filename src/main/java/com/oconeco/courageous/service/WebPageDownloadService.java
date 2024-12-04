package com.oconeco.courageous.service;

import java.io.IOException;

public interface WebPageDownloadService {
    String download(String uri) throws IOException;
}
