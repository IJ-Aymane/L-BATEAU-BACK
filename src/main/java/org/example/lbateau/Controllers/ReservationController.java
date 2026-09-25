package org.example.lbateau.Controllers;

import org.example.lbateau.DTO.ReservationDTOs;
import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDTOs.ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(reservationService::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservationDTOs.ReservationResponse getReservationById(@PathVariable String id) {
        return reservationService.toResponse(reservationService.getReservationOrThrow(id));
    }

    @GetMapping("/user/{userId}")
    public List<ReservationDTOs.ReservationResponse> getReservationsByUserId(@PathVariable String userId) {
        return reservationService.getReservationsByUserId(userId).stream()
                .map(reservationService::toResponse)
                .toList();
    }

    @GetMapping("/client/{clientId}")
    public List<ReservationDTOs.ReservationResponse> getReservationsByClientId(@PathVariable String clientId) {
        return reservationService.getReservationsByClientId(clientId).stream()
                .map(reservationService::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationDTOs.ReservationResponse> createReservation(@RequestBody ReservationDTOs.CreateReservationRequest request) {
        Reservation saved = reservationService.createReservation(request);
        return ResponseEntity.ok(reservationService.toResponse(saved));
    }

    @GetMapping("/{id}/invoice")
    public ReservationDTOs.InvoiceResponse getInvoice(@PathVariable String id) {
        return reservationService.buildInvoice(id);
    }

    @PutMapping("/{id}")
    public ReservationDTOs.ReservationResponse updateReservation(@PathVariable String id, @RequestBody Reservation reservation) {
        return reservationService.toResponse(reservationService.updateReservation(id, reservation));
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
    }
}
