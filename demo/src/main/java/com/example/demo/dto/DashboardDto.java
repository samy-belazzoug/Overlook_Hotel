package com.example.demo.dto;

public class DashboardDto {
    private long confirmedReservations;
    private long cancelledReservations;
    private double revenus;
    private double tauxOccupation;

    public long getConfirmedReservations() {
        return confirmedReservations;
    }

    public void setConfirmedReservations(long confirmedReservations) {
        this.confirmedReservations = confirmedReservations;
    }

    public long getCancelledReservations() {
        return cancelledReservations;
    }

    public void setCancelledReservations(long cancelledReservations) {
        this.cancelledReservations = cancelledReservations;
    }

    public double getRevenus() {
        return revenus;
    }

    public void setRevenus(double revenus) {
        this.revenus = revenus;
    }

    public double getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(double tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }
}
