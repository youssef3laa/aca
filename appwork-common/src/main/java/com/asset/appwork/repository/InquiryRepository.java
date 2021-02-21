package com.asset.appwork.repository;


import com.asset.appwork.model.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface InquiryRepository extends GenericRepository<Inquiry, Long> {

     @Query("SELECT inquiry FROM Inquiry inquiry  WHERE( inquiry.fullName LIKE %:search% OR inquiry.birthPlace LIKE %:search%) and inquiry.incomingRegistration.id=:incomingRegistrationId order by inquiry.fullName")
     Page<Inquiry> getInquiriesByIncomingRegistrationId(Long incomingRegistrationId, String search, Pageable pageable);
}
