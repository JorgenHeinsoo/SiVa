package ee.openeid.siva.integrationtest;

import ee.openeid.siva.integrationtest.configuration.IntegrationTest;
import ee.openeid.siva.validation.document.report.QualifiedReport;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
public class BdocValidationFail extends SiVaRestTests{

    @Before
    public void DirectoryBackToDefault() {
        setTestFilesDirectory(DEFAULT_TEST_FILES_DIRECTORY);
    }

    private static final String DEFAULT_TEST_FILES_DIRECTORY = "bdoc/live/timemark/";

    private String testFilesDirectory = DEFAULT_TEST_FILES_DIRECTORY;

    public void setTestFilesDirectory(String testFilesDirectory) {
        this.testFilesDirectory = testFilesDirectory;
    }

    /***
     * TestCaseID: Bdoc-ValidationFail-1
     *
     * TestType: Automated
     *
     * RequirementID:
     *
     * Title: Bdoc with single invalid signature
     *
     * Expected Result: The document should fail the validation
     *
     * File: IB-3960_bdoc2.1_TSA_SignatureValue_altered.bdoc
     ***/
    @Test
    public void InvalidSingleSignature() {
        QualifiedReport report = postForReport("IB-3960_bdoc2.1_TSA_SignatureValue_altered.bdoc");
        assertAllSignaturesAreInvalid(report);
        assertTrue(report.getSignatures().get(0).getErrors().size() == 2);
    }

    /***
     * TestCaseID: Bdoc-ValidationFail-2
     *
     * TestType: Automated
     *
     * RequirementID:
     *
     * Title: Bdoc with multiple invalid signatures
     *
     * Expected Result: The document should fail the validation
     *
     * File: BdocMultipleSignaturesInvalid.bdoc
     ***/
    @Test
    public void InvalidMultipleSignatures() {
        setTestFilesDirectory("bdoc/test/timemark/");
        assertAllSignaturesAreInvalid(postForReport("BdocMultipleSignaturesInvalid.bdoc"));
    }

    /***
     * TestCaseID: Bdoc-ValidationFail-3
     *
     * TestType: Automated
     *
     * RequirementID:
     *
     * Title: Bdoc with multiple signatures both valid and invalid
     *
     * Expected Result: The document should fail the validation
     *
     * File: BdocMultipleSignaturesMixedWithValidAndInvalid.bdoc
     ***/
    @Test
    public void InvalidAndValidMultipleSignatures() {
        setTestFilesDirectory("bdoc/test/timemark/");
        assertSomeSignaturesAreValid(postForReport("BdocMultipleSignaturesMixedWithValidAndInvalid.bdoc"),2);
    }

    /***
     * TestCaseID: Bdoc-ValidationFail-4
     *
     * TestType: Automated
     *
     * RequirementID:
     *
     * Title: Bdoc with no signatures
     *
     * Expected Result: The document should fail the validation
     *
     * File: BdocContainerNoSignature.bdoc
     ***/
    @Test
    public void NoSignatures() {
        setTestFilesDirectory("document_format_test_files/");
        assertAllSignaturesAreInvalid(postForReport("BdocContainerNoSignature.bdoc"));
    }

    /***
     * TestCaseID: Bdoc-ValidationFail-5
     *
     * TestType: Automated
     *
     * RequirementID:
     *
     * Title: Bdoc with invalid mimetype in manifest
     *
     * Expected Result: document malformed error should be returned
     *
     * File: 23147_weak-warning-sha1-invalid-mimetype-in-manifest.bdoc
     ***/
    @Test
    public void malformedBdocWithInvalidMimetypeInManifest() {
        post(validationRequestFor("23147_weak-warning-sha1-invalid-mimetype-in-manifest.bdoc", "simple"))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("requestErrors[0].key", Matchers.is(DOCUMENT))
                .body("requestErrors[0].message", Matchers.containsString(DOCUMENT_MALFORMED_OR_NOT_MATCHING_DOCUMENT_TYPE));
    }


    @Override
    protected String getTestFilesDirectory() {
        return testFilesDirectory;
    }
}