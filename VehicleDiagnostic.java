package com.example.ovbha;

public class VehicleDiagnostic {

    public String diagnoseVehicleIssues() {
        // Simulates data collection from sensors
        double engineTemperature = getRandomValue(50, 120);
        double oilPressure = getRandomValue(10, 60);
        double batteryVoltage = getRandomValue(11, 14);

        // Checks collected values and returns information about potential problems
        StringBuilder issues = new StringBuilder();
        if (engineTemperature > 100) {
            issues.append("Engine temperature is high. Check cooling system.\n");
        }
        if (oilPressure < 20) {
            issues.append("Low oil pressure detected. Check engine oil level.\n");
        }
        if (batteryVoltage < 12) {
            issues.append("Low battery voltage detected. Check charging system.\n");
        }

        // Returns the diagnostic result
        if (issues.length() > 0) {
            return issues.toString();
        } else {
            return "No issues found. Vehicle is operating normally.";
        }
    }

    //Generate random values for simulations
    private double getRandomValue(double min, double max) {
        return min + Math.random() * (max - min);
    }
}
