package com.smockin.admin.controller;

import com.smockin.admin.dto.MockImportConfigDTO;
import com.smockin.admin.dto.response.SimpleMessageResponseDTO;
import com.smockin.admin.enums.MockImportKeepStrategyEnum;
import com.smockin.admin.exception.MockExportException;
import com.smockin.admin.exception.MockImportException;
import com.smockin.admin.exception.RecordNotFoundException;
import com.smockin.admin.exception.ValidationException;
import com.smockin.admin.service.MockDefinitionImportExportService;
import com.smockin.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

/**
 * Created by mgallina.
 */
@Controller
public class MockDefinitionImportExportController {

    private static final String ERROR_MSG_NOTHING_SPECIFIED_TO_EXPORT = "Nothing specified to export";
    private static final String ERROR_MSG_SINGLE_MOCK_CAN_EXPORT_ONLY_ONE_MOCK_AT_A_TIME = "SingleMock can export only one mock at a time";
    @Autowired
    private MockDefinitionImportExportService mockDefinitionImportExportService;

    @RequestMapping(path="/mock/import", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<SimpleMessageResponseDTO> importMocks(@RequestHeader(value = GeneralUtils.OAUTH_HEADER_NAME, required = false) final String bearerToken,
                                                                              @RequestHeader(value = GeneralUtils.KEEP_EXISTING_HEADER_NAME) final boolean keepExisting,
                                                                              @RequestParam("file") final MultipartFile file)
                                                            throws ValidationException, MockImportException, RecordNotFoundException {

        final String token = GeneralUtils.extractOAuthToken(bearerToken);

        final MockImportConfigDTO configDTO = (keepExisting)
                ? new MockImportConfigDTO(MockImportKeepStrategyEnum.RENAME_NEW )
                : new MockImportConfigDTO();

        return ResponseEntity.ok(new SimpleMessageResponseDTO(mockDefinitionImportExportService
                .importFile(file, configDTO, token)));
    }

    @RequestMapping(path="/mock/export/{serverType}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ResponseEntity<String> exportMocks(@PathVariable("serverType") final String serverType,
                                                                       @RequestHeader(value = GeneralUtils.OAUTH_HEADER_NAME, required = false) final String bearerToken,
                                                                       @RequestBody final List<String> exports)
                                                                throws MockExportException, RecordNotFoundException {

        final String token = GeneralUtils.extractOAuthToken(bearerToken);

        final String exportFileName = mockDefinitionImportExportService.exportZipFileNamePrefix
                + GeneralUtils.createFileNameUniqueTimeStamp()
                + mockDefinitionImportExportService.exportZipFileNameExt;

        return ResponseEntity.ok()
                .header("Content-Type", "application/zip")
                .header("Content-Disposition", "attachment; filename=\"" + exportFileName + "\"")
                .body(mockDefinitionImportExportService.export(exports, token));
    }

    @RequestMapping(path="/mock/export-single/{serverType}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> exportSingleMock(@PathVariable("serverType") final String serverType,
                                                                       @RequestHeader(value = GeneralUtils.OAUTH_HEADER_NAME, required = false) final String bearerToken,
                                                                       @RequestBody final List<String> exports)
                                                                throws MockExportException, RecordNotFoundException {

        final String token = GeneralUtils.extractOAuthToken(bearerToken);

        if (exports == null) {
            throw new MockExportException(ERROR_MSG_NOTHING_SPECIFIED_TO_EXPORT);
        }
        if (exports.size() > 1) {
            throw new MockExportException(ERROR_MSG_SINGLE_MOCK_CAN_EXPORT_ONLY_ONE_MOCK_AT_A_TIME);
        }

        String json = mockDefinitionImportExportService.exportSingleMock(exports.get(0), token);
        json = GeneralUtils.jsonPrettyPrint(json);
        final String exportFileName = mockDefinitionImportExportService.exportZipFileNamePrefix
                + exports.get(0)
                + mockDefinitionImportExportService.exportFileNameExt;

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header("Content-Disposition", "attachment; filename=\"" + exportFileName + "\"")
                .body(Base64.getEncoder().encodeToString(json.getBytes()));
    }

}
