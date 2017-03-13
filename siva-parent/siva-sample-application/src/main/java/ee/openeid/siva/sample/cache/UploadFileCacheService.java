/*
 * Copyright 2016 Riigi Infosüsteemide Amet
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */

package ee.openeid.siva.sample.cache;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileCacheService {
    UploadedFile addUploadedFile(final long timestamp, final MultipartFile file, String encodedFilename) throws IOException;
    UploadedFile getUploadedFile(long timestamp);
    void deleteUploadedFile(final long timestamp);
}