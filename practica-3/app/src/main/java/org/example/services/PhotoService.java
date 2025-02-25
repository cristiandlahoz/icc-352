package org.example.services;

import java.util.List;
import java.util.Optional;
import org.example.models.Photo;
import org.example.repository.PhotoRepository;

public class PhotoService {
  private final PhotoRepository photoRepository;

  public PhotoService(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  public List<Photo> getAllPhotos() {
    return photoRepository.findAll();
  }

  public Optional<Photo> getPhotoById(Long id) {
    return photoRepository.findById(id);
  }

  public Photo createPhoto(Photo photo) {
    return photoRepository.save(photo);
  }

  public Optional<Photo> updatePhoto(Photo photo) {
    if (photoRepository.findById(photo.getId()).isPresent()) {
      return Optional.of(photoRepository.save(photo));
    }
    return Optional.empty();
  }

  public boolean deletePhoto(Long id) {
    if (photoRepository.findById(id).isPresent()) {
      photoRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
