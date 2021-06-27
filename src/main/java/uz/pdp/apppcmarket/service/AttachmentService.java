package uz.pdp.apppcmarket.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.apppcmarket.entity.Attachment;
import uz.pdp.apppcmarket.entity.AttachmentContent;
import uz.pdp.apppcmarket.payload.ApiResponse;
import uz.pdp.apppcmarket.repository.AttachmentContentRepository;
import uz.pdp.apppcmarket.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    @SneakyThrows
    public ApiResponse uploadFile(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            Attachment attachment = new Attachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());
            Attachment savedAttachment = attachmentRepository.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setContent(file.getBytes());
            attachmentContent.setAttachment(savedAttachment);
            attachmentContentRepository.save(attachmentContent);
        }
        return new ApiResponse("Fayllar saqlandi!", true);
    }


    public List<Attachment> getAllFileInfo() {
        return attachmentRepository.findAll();
    }

    public Attachment getFileInfo(Integer id) {
        return attachmentRepository.findById(id).orElseGet(Attachment::new);
    }

    @SneakyThrows
    public ApiResponse getFileContent(Integer id, HttpServletResponse response) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (!optionalAttachment.isPresent())
            return new ApiResponse("Attachment not found", false);
        Attachment attachment = optionalAttachment.get();
        Optional<AttachmentContent> contentOptional = attachmentContentRepository.findByAttachmentId(attachment.getId());
        if (!contentOptional.isPresent())
            return new ApiResponse("Content not found", false);
        AttachmentContent attachmentContent = contentOptional.get();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getName() + "\"");
        FileCopyUtils.copy(attachmentContent.getContent(), response.getOutputStream());
        return new ApiResponse("Content sent", true);
    }

    @SneakyThrows
    public ApiResponse editFile(Integer id, MultipartHttpServletRequest request) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if(!optionalAttachment.isPresent())
            return new ApiResponse("Attachment not found!", false);
        Attachment editingAttachment = optionalAttachment.get();
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            editingAttachment.setContentType(file.getContentType());
            editingAttachment.setSize(file.getSize());
            editingAttachment.setName(file.getOriginalFilename());
            Attachment savedAttachment = attachmentRepository.save(editingAttachment);
            Optional<AttachmentContent> contentOptional = attachmentContentRepository.findByAttachmentId(savedAttachment.getId());
            if(!contentOptional.isPresent())
                return new ApiResponse("Content not found!", false);
            AttachmentContent attachmentContent = contentOptional.get();
            attachmentContent.setContent(file.getBytes());
            attachmentContentRepository.save(attachmentContent);
        }
        return new ApiResponse("File edited!", true);
    }

    public ApiResponse deleteFile(Integer id) {
        Optional<AttachmentContent> contentOptional = attachmentContentRepository.findByAttachmentId(id);
        if(!contentOptional.isPresent())
            return new ApiResponse("Content not found!", false);
        attachmentContentRepository.delete(contentOptional.get());
        boolean existsById = attachmentRepository.existsById(id);
        if(!existsById)
            return new ApiResponse("Attachment not found!", false);
        attachmentRepository.deleteById(id);
        return new ApiResponse("Attachment deleted!", true);
    }
}
