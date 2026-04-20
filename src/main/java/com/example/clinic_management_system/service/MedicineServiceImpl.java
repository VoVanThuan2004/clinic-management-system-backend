package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicineRequest;
import com.example.clinic_management_system.dto.response.MedicineResponse;
import com.example.clinic_management_system.dto.response.UploadResult;
import com.example.clinic_management_system.entity.Category;
import com.example.clinic_management_system.entity.Medicine;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.MedicineMapper;
import com.example.clinic_management_system.repository.CategoryRepository;
import com.example.clinic_management_system.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final MedicineMapper medicineMapper;

    @Override
    @Transactional
    public void addMedicine(MedicineRequest medicineRequest, MultipartFile file) {
        if (medicineRequest.getMedicineName() == null || medicineRequest.getCategoryId() == null
            || medicineRequest.getSellingPrice() == null || medicineRequest.getOriginalPrice() == null
                || medicineRequest.getStockQuantity() == null || medicineRequest.getUnit() == null
                || medicineRequest.getDescription() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // 1. Kiểm tra có upload file
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Vui lòng upload file ảnh thuốc");
        }

        // 2. Kiểm tra danh mục có tồn tại
        Optional<Category> category = categoryRepository.findById(medicineRequest.getCategoryId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Danh mục không tồn tại");
        }

        // 3. Kiểm tra giá bán, giá gốc
        if (medicineRequest.getOriginalPrice() > medicineRequest.getSellingPrice()) {
            throw new BadRequestException("Giá gốc phải nhỏ hơn giá bán");
        }

        // 4. Kiểm tra tồn kho
        if (medicineRequest.getStockQuantity() <= 0) {
            throw new BadRequestException("Số lượng tồn kho phải lớn hơn 0");
        }

        // 5. Upload file
        UploadResult uploadResult = cloudinaryService.uploadFile(file);

        // 6. Tạo thuốc
        Medicine medicine = medicineMapper.toEntity(medicineRequest);
        medicine.setCategory(category.get());
        medicine.setPublicId(uploadResult.getPublicId());
        medicine.setImage(uploadResult.getSecureUrl());

        medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public void updateMedicine(String medicineId, MedicineRequest medicineRequest, MultipartFile file) {
        // 1. Kiểm tra medicine có tồn tại
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);
        if (medicine.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc không tồn tại");
        }

        // 2. Kiểm tra category
        Optional<Category> category = categoryRepository.findById(medicineRequest.getCategoryId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Danh mục không tồn tại");
        }

        // 3. Kiểm tra có upload file
        UploadResult uploadResult = new UploadResult();
        if (file != null) {
            // 3.1 Xóa ảnh cũ
            cloudinaryService.deleteFile(medicine.get().getPublicId());
            // 3.2 Upload ảnh mới
            uploadResult = cloudinaryService.uploadFile(file);
        }

        // 3. Cập nhật thuốc
        medicine.get().setCategory(category.get());
        medicine.get().setMedicineName(medicineRequest.getMedicineName());
        medicine.get().setSellingPrice(medicineRequest.getSellingPrice());
        medicine.get().setOriginalPrice(medicineRequest.getOriginalPrice());
        medicine.get().setStockQuantity(medicineRequest.getStockQuantity());
        medicine.get().setUnit(medicineRequest.getUnit());
        medicine.get().setDescription(medicineRequest.getDescription());

        if (uploadResult.getPublicId() != null && uploadResult.getSecureUrl() != null) {
            medicine.get().setPublicId(uploadResult.getPublicId());
            medicine.get().setImage(uploadResult.getSecureUrl());
        }
        medicineRepository.save(medicine.get());
    }

    @Override
    public void deleteMedicine(String medicineId) {
        // 1. Kiểm tra medicine có tồn tại
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);
        if (medicine.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc không tồn tại");
        }

        // 2. Xóa ảnh trên cloudinary
        cloudinaryService.deleteFile(medicine.get().getPublicId());

        medicineRepository.deleteById(medicineId);
    }

    @Override
    public Page<MedicineResponse> getMedicinesByCategory(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 1. Query lấy data
        Page<Medicine> medicines = medicineRepository.findAllByCategoryId(categoryId, pageable);

        // 2. Mapping data trả về
        Page<MedicineResponse> medicineResponses = medicines.map(medicineMapper::toResponse);

        return medicineResponses;
    }
}
