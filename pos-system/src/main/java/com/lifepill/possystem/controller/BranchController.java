package com.lifepill.possystem.controller;

import com.lifepill.possystem.dto.BranchDTO;
import com.lifepill.possystem.dto.EmployerDTO;
import com.lifepill.possystem.dto.requestDTO.BranchUpdateDTO;
import com.lifepill.possystem.dto.responseDTO.PharmacyBranchResponseDTO;
import com.lifepill.possystem.service.BranchService;
import com.lifepill.possystem.service.EmployerService;
import com.lifepill.possystem.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class for managing branch-related operations.
 */
@RestController
@RequestMapping("lifepill/v1/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private EmployerService employerService;

    /**
     * Endpoint for saving a new branch along with an image.
     *
     * @param image     The image file of the branch
     * @param branchDTO The DTO containing branch details
     * @return A success message indicating that the branch has been saved
     */
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveBranch(@RequestParam("image") MultipartFile image, @ModelAttribute BranchDTO branchDTO) {
        branchService.saveBranch(branchDTO, image);
        return "saved";
    }

    /**
     * Endpoint for viewing the image of a branch.
     *
     * @param branchId The ID of the branch
     * @return ResponseEntity containing the branch image data or a not found status
     */
    @GetMapping("/view-image/{branchId}")
    public ResponseEntity<byte[]> viewImage(@PathVariable int branchId) {
        byte[] imageData = branchService.getImageData(branchId);

        if (imageData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust the media type
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving all branches.
     *
     * @return ResponseEntity containing a list of all branches
     */
    @GetMapping(path = "/get-all-branches")
    public ResponseEntity<StandardResponse> getAllBranches() {
        List<BranchDTO> allBranches = branchService.getAllBranches();
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "SUCCESS", allBranches),
                HttpStatus.OK
        );
    }

    /**
     * Endpoint for retrieving a branch by its ID.
     *
     * @param branchId The ID of the branch
     * @return The DTO representing the branch
     */
    @GetMapping(path = "/get-by-id", params = "id")
    @Transactional
    public BranchDTO getBranchById(@RequestParam(value = "id") int branchId) {
        BranchDTO branchDTO = branchService.getBranchById(branchId);
        return branchDTO;
    }

    /**
     * Endpoint for deleting a branch by its ID.
     *
     * @param branchId The ID of the branch to be deleted
     * @return A message indicating the deletion status
     */
    @DeleteMapping(path = "/delete-branch/{id}")
    public String deleteBranch(@PathVariable(value = "id") int branchId) {
        String deleted = branchService.deleteBranch(branchId);
        return deleted;
    }

    /**
     * Endpoint for updating a branch along with an image.
     *
     * @param branchId       The ID of the branch to be updated
     * @param image          The updated image file of the branch
     * @param branchUpdateDTO The DTO containing updated branch details
     * @return A message indicating that the branch has been updated
     */
    @PutMapping(value = "/update-branch/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public String updateBranch(
            @PathVariable(value = "id") int branchId,
            @RequestParam("image") MultipartFile image,
            @ModelAttribute BranchUpdateDTO branchUpdateDTO) {
        branchService.updateBranch(branchId, branchUpdateDTO, image);
        return "updated";
    }

    /**
     * Endpoint for updating the image of a branch.
     *
     * @param branchId The ID of the branch to update the image for
     * @param image    The updated image file of the branch
     * @return A message indicating that the branch image has been updated
     */
    @PutMapping(value = "/update-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public String updateBranchImage(
            @PathVariable(value = "id") int branchId,
            @RequestParam("image") MultipartFile image) {
        branchService.updateBranchImage(branchId, image);
        return "image updated";
    }

    /**
     * Endpoint for updating a branch without updating its image.
     *
     * @param branchId       The ID of the branch to be updated
     * @param branchUpdateDTO The DTO containing updated branch details
     * @return A message indicating that the branch has been updated
     */
    @PutMapping(value = "/update/{id}")
    @Transactional
    public String updateBranchWithoutImage(
            @PathVariable(value = "id") int branchId,
            @ModelAttribute BranchUpdateDTO branchUpdateDTO) {
        branchService.updateBranchWithoutImage(branchId, branchUpdateDTO);
        return "branch updated";
    }

    /**
     * Endpoint for retrieving all employers (cashiers) associated with a specific branch.
     *
     * @param branchId The ID of the branch
     * @return ResponseEntity containing a list of employer DTOs
     */
    @GetMapping("/employer/by-branch/{branchId}")
    public ResponseEntity<List<EmployerDTO>> getAllCashiersByBranchId(@PathVariable int branchId) {
        List<EmployerDTO> employerDTOS = employerService.getAllEmployerByBranchId(branchId);
        return new ResponseEntity<>(employerDTOS, HttpStatus.OK);
    }

    /**
     * Test endpoint for branch operations.
     *
     * @return ResponseEntity containing a success message
     */
    @GetMapping("/branch-test")
    public ResponseEntity<String> testEmployer() {
        return ResponseEntity.ok("Branch test successful");
    }



}
