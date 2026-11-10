    package ms_suivi.service;

    import ms_suivi.entity.PhotoSuivi;

    import java.util.List;

    public interface PhotoSuiviService {

        PhotoSuivi createPhotoSuivi(PhotoSuivi photoSuivi);

        PhotoSuivi updatePhotoSuivi(Long id, PhotoSuivi photoSuivi);

        void deletePhotoSuivi(Long id);

        PhotoSuivi getPhotoSuiviById(Long id);

        List<PhotoSuivi> getAllPhotos();

        List<PhotoSuivi> getPhotosBySuiviId(Long suiviId);

        List<PhotoSuivi> getPhotosByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
    }
