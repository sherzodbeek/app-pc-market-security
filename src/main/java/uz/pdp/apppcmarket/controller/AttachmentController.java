package uz.pdp.apppcmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.apppcmarket.entity.Attachment;
import uz.pdp.apppcmarket.payload.ApiResponse;
import uz.pdp.apppcmarket.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<?> uploadFile(MultipartHttpServletRequest request) {
        ApiResponse apiResponse = attachmentService.uploadFile(request);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllFileInfo() {
        List<Attachment> attachments = attachmentService.getAllFileInfo();
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getFileInfo(@PathVariable Integer id) {
        Attachment attachment = attachmentService.getFileInfo(id);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<?> getContent(@PathVariable Integer id, HttpServletResponse response) {
        ApiResponse apiResponse = attachmentService.getFileContent(id, response);
        return ResponseEntity.status(apiResponse.isSuccess()?200:404).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editFile(@PathVariable Integer id, MultipartHttpServletRequest request) {
        ApiResponse apiResponse = attachmentService.editFile(id, request);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer id) {
        ApiResponse apiResponse = attachmentService.deleteFile(id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }
}
