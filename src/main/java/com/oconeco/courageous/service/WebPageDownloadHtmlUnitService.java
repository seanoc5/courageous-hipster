package com.oconeco.courageous.service;

import java.io.IOException;

public interface WebPageDownloadHtmlUnitService {
    String downloadWithHtmlUnit(String uri) throws IOException;
}
