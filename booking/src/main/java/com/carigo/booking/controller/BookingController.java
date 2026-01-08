// package com.carigo.booking.controller;

// import com.carigo.booking.dto.BookingRequestDTO;
// import com.carigo.booking.dto.BookingResponseDTO;
// import com.carigo.booking.dto.ReviewRequestDto;
// import com.carigo.booking.dto.TripExtensionRequest;
// import com.carigo.booking.helper.BookingStatus;
// import com.carigo.booking.service.BookingService;

// import lombok.RequiredArgsConstructor;

// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/bookings")
// @RequiredArgsConstructor
// @CrossOrigin
// public class BookingController {

// private final BookingService bookingService;

// // ======================= CREATE BOOKING ==========================
// @PostMapping("/create")
// public ResponseEntity<BookingResponseDTO> createBooking(
// @RequestBody BookingRequestDTO requestDTO) {

// BookingResponseDTO response = bookingService.createBooking(requestDTO);
// return ResponseEntity.ok(response);
// }

// // ======================== APPROVE BOOKING ========================
// @PutMapping("/{id}/approve")
// public ResponseEntity<BookingResponseDTO> approveBooking(@PathVariable Long
// id) {
// return ResponseEntity.ok(bookingService.approveBooking(id));
// }

// // ======================== REJECT BOOKING =========================
// @PutMapping("/{id}/reject")
// public ResponseEntity<BookingResponseDTO> rejectBooking(@PathVariable Long
// id) {
// return ResponseEntity.ok(bookingService.rejectBooking(id));
// }

// // ====================== PAYMENT SUCCESS ==========================
// @PutMapping("/{id}/payment-success")
// public ResponseEntity<BookingResponseDTO> paymentSuccess(@PathVariable Long
// id) {
// return ResponseEntity.ok(bookingService.paymentSuccess(id));
// }

// // ======================== TRIP START =============================
// @PutMapping("/{id}/start-trip")
// public ResponseEntity<BookingResponseDTO> startTrip(@PathVariable Long id) {
// return ResponseEntity.ok(bookingService.startTrip(id));
// }

// // ======================== TRIP END ===============================
// @PutMapping("/{id}/end-trip")
// public ResponseEntity<BookingResponseDTO> endTrip(@PathVariable Long id) {
// return ResponseEntity.ok(bookingService.endTrip(id));
// }

// // ======================== CANCEL BOOKING =========================
// @PutMapping("/{id}/cancel")
// public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long
// id) {
// return ResponseEntity.ok(bookingService.cancelBooking(id));
// }

// @GetMapping("/owner/{ownerId}/requests")
// public ResponseEntity<List<BookingResponseDTO>>
// getOwnerBookingRequests(@PathVariable Long ownerId) {
// return ResponseEntity.ok(bookingService.getBookingRequestsForOwner(ownerId));
// }

// @PutMapping("/{id}/extend")
// public ResponseEntity<BookingResponseDTO> extendTrip(
// @PathVariable Long id,
// @RequestBody TripExtensionRequest req) {
// return ResponseEntity.ok(bookingService.extendTrip(id, req));
// }

// @PostMapping("/{id}/review")
// public ResponseEntity<Void> submitReview(
// @PathVariable Long id,
// @RequestBody ReviewRequestDto dto) {

// bookingService.submitReview(id, dto);
// return ResponseEntity.ok().build();
// }

// // ================= RENTER ALL BOOKINGS =================
// @GetMapping("/renter/{renterId}")
// public ResponseEntity<List<BookingResponseDTO>> getBookingsForRenter(
// @PathVariable Long renterId) {

// return ResponseEntity.ok(
// bookingService.getBookingsForRenter(renterId));
// }

// // ================= RENTER ACTIVE BOOKINGS =================
// @GetMapping("/renter/{renterId}/active")
// public ResponseEntity<List<BookingResponseDTO>> getActiveBookingsForRenter(
// @PathVariable Long renterId) {

// return ResponseEntity.ok(
// bookingService.getActiveBookingsForRenter(renterId));
// }

// // ================= OWNER ALL BOOKINGS =================
// @GetMapping("/owner/{ownerId}")
// public ResponseEntity<List<BookingResponseDTO>> getBookingsForOwner(
// @PathVariable Long ownerId) {

// return ResponseEntity.ok(
// bookingService.getBookingsForOwner(ownerId));
// }

// // ================= OWNER ONGOING BOOKINGS =================
// @GetMapping("/owner/{ownerId}/ongoing")
// public ResponseEntity<List<BookingResponseDTO>> getOwnerOngoingBookings(
// @PathVariable Long ownerId) {

// return ResponseEntity.ok(
// bookingService.getOngoingBookingsForOwner(ownerId));
// }

// // ================= OWNER COMPLETED BOOKINGS =================
// @GetMapping("/owner/{ownerId}/completed")
// public ResponseEntity<List<BookingResponseDTO>> getOwnerCompletedBookings(
// @PathVariable Long ownerId) {

// return ResponseEntity.ok(
// bookingService.getCompletedBookingsForOwner(ownerId));
// }

// @GetMapping("/admin/bookings")
// public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
// @RequestParam(defaultValue = "0") int page,
// @RequestParam(defaultValue = "20") int size) {

// return ResponseEntity.ok(
// bookingService.getAllBookings(page, size));
// }

// @GetMapping("/admin/bookings/status/{status}")
// public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(
// @PathVariable BookingStatus status) {

// return ResponseEntity.ok(
// bookingService.getBookingsByStatus(status));
// }

// @PostMapping("/admin/bookings/{id}/force-cancel")
// public ResponseEntity<BookingResponseDTO> forceCancel(
// @PathVariable Long id) {

// return ResponseEntity.ok(
// bookingService.forceCancelBooking(id));
// }

// @GetMapping("/bookings/vehicle/{vehicleId}/history")
// public ResponseEntity<List<BookingResponseDTO>> getVehicleHistory(
// @PathVariable String vehicleId) {

// return ResponseEntity.ok(
// bookingService.getVehicleBookingHistory(vehicleId));
// }

// @GetMapping("/count/owner/{ownerId}")
// public ResponseEntity<Long> countByOwner(@PathVariable Long ownerId) {

// return ResponseEntity.ok(
// bookingService.countBookingsForOwner(ownerId));
// }

// @GetMapping("/count/renter/{renterId}")
// public ResponseEntity<Long> countByRenter(@PathVariable Long renterId) {

// return ResponseEntity.ok(
// bookingService.countBookingsForRenter(renterId));
// }

// @GetMapping("/vehicle/{vehicleId}/availability")
// public ResponseEntity<Boolean> checkVehicleAvailability(
// @PathVariable String vehicleId) {

// return ResponseEntity.ok(
// bookingService.isVehicleAvailable(vehicleId));
// }

// }
