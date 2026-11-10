package ms_suivi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms_suivi.entity.PhotoSuivi;
import ms_suivi.service.PhotoSuiviService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/photos", "/api/suivis/photos"})
@RequiredArgsConstructor
public class PhotoSuiviController {

    private final PhotoSuiviService photoSuiviService;

    @PostMapping
    public ResponseEntity<PhotoSuivi> createPhotoSuivi(@Valid @RequestBody PhotoSuivi photoSuivi) {
        PhotoSuivi createdPhoto = photoSuiviService.createPhotoSuivi(photoSuivi);
        return new ResponseEntity<>(createdPhoto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoSuivi> updatePhotoSuivi(
            @PathVariable Long id,
            @Valid @RequestBody PhotoSuivi photoSuivi) {
        PhotoSuivi updatedPhoto = photoSuiviService.updatePhotoSuivi(id, photoSuivi);
        return ResponseEntity.ok(updatedPhoto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhotoSuivi(@PathVariable Long id) {
        photoSuiviService.deletePhotoSuivi(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoSuivi> getPhotoSuiviById(@PathVariable Long id) {
        return ResponseEntity.ok(photoSuiviService.getPhotoSuiviById(id));
    }

    @GetMapping
    public ResponseEntity<List<PhotoSuivi>> getAllPhotos() {
        List<PhotoSuivi> photos = photoSuiviService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/suivi/{suiviId}")
    public ResponseEntity<List<PhotoSuivi>> getPhotosBySuiviId(@PathVariable Long suiviId) {
        List<PhotoSuivi> photos = photoSuiviService.getPhotosBySuiviId(suiviId);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PhotoSuivi>> getPhotosByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<PhotoSuivi> photos = photoSuiviService.getPhotosByDateRange(start, end);
        return ResponseEntity.ok(photos);
    }
}
