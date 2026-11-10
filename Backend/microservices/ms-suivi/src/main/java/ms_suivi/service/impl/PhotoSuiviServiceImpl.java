package ms_suivi.service.impl;

import lombok.RequiredArgsConstructor;
import ms_suivi.entity.PhotoSuivi;
import ms_suivi.exception.ResourceNotFoundException;
import ms_suivi.repository.PhotoSuiviRepository;
import ms_suivi.service.PhotoSuiviService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoSuiviServiceImpl implements PhotoSuiviService {

    private final PhotoSuiviRepository photoSuiviRepository;

    @Override
    public PhotoSuivi createPhotoSuivi(PhotoSuivi photoSuivi) {
        return photoSuiviRepository.save(photoSuivi);
    }

    @Override
    public PhotoSuivi updatePhotoSuivi(Long id, PhotoSuivi photoSuivi) {
        PhotoSuivi existingPhoto = photoSuiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PhotoSuivi", id));

        existingPhoto.setSuivi(photoSuivi.getSuivi());
        existingPhoto.setUrlPhoto(photoSuivi.getUrlPhoto());
        existingPhoto.setDateAjout(photoSuivi.getDateAjout());

        return photoSuiviRepository.save(existingPhoto);
    }

    @Override
    public void deletePhotoSuivi(Long id) {
        if (!photoSuiviRepository.existsById(id)) {
            throw new ResourceNotFoundException("PhotoSuivi", id);
        }
        photoSuiviRepository.deleteById(id);
    }

    @Override
    public PhotoSuivi getPhotoSuiviById(Long id) {
        return photoSuiviRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PhotoSuivi", id));
    }

    @Override
    public List<PhotoSuivi> getAllPhotos() {
        return photoSuiviRepository.findAll();
    }

    @Override
    public List<PhotoSuivi> getPhotosBySuiviId(Long suiviId) {
        return photoSuiviRepository.findBySuiviId(suiviId);
    }

    @Override
    public List<PhotoSuivi> getPhotosByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return photoSuiviRepository.findByDateAjoutBetween(startDate, endDate);
    }
}
