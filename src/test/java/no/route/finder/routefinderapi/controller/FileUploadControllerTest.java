package no.route.finder.routefinderapi.controller;

import no.route.finder.routefinderapi.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileService fileService;

    @Test
    public void should_return_200_if_file_upload_succeed() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "spidercase.csv", MULTIPART_FORM_DATA_VALUE, "some xml".getBytes());
        doNothing().when(fileService).process(file);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload").file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_400_if_file_extension_is_incorrect() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "spidercase.txt", MULTIPART_FORM_DATA_VALUE, "some xml".getBytes());
        doNothing().when(fileService).process(file);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("File extension must be .csv or .CSV"));
    }

}