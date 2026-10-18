import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;

/**
 * Smart Car Plate Identification System (VRIS) demonstration.
 * Uses only standard Java 17+ APIs and fictional vehicle/owner records.
 * IMPORTANT: Not connected to any official vehicle registration system.
 */
public class SmartCarPlateSystem {
    private static final Path DATA_FILE = Paths.get("vehicles.csv");
    private static final Map<String, Vehicle> vehicles = new LinkedHashMap<>();

    static class Vehicle {
        final String plate, make, model, year, colour, owner, contact;
        final String registrationStatus, licenceExpiry;
        Vehicle(String[] fields) {
            plate = fields[0]; make = fields[1]; model = fields[2];
            year = fields[3]; colour = fields[4]; owner = fields[5];
            contact = fields[6]; registrationStatus = fields[7]; licenceExpiry = fields[8];
        }
    }

    private static String normalize(String plate) {
        return plate.trim().toUpperCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private static void loadVehicles() throws IOException {
        if (Files.notExists(DATA_FILE)) {
            // Entirely fictional identities and demonstration registration numbers.
            List<String> sample = List.of(
                "plate,make,model,year,colour,owner,contact,registrationStatus,licenceExpiry",
                "ND 123 GP,Toyota,Corolla,2021,White,Thando Mkhize,DEMO-001,Active,2027-06-30",
                "CA 456 WC,Volkswagen,Polo,2020,Blue,Lerato Dlamini,DEMO-002,Active,2027-03-31",
                "KZN 789 EC,Ford,Ranger,2019,Silver,Sam Ndlovu,DEMO-003,Expired,2025-12-31"
            );
            Files.write(DATA_FILE, sample, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        }
        List<String> lines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
        vehicles.clear();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",", -1);
            if (parts.length != 9) {
                System.err.println("Skipping invalid row " + (i + 1) + " (expected 9 columns)");
                continue;
            }
            Vehicle vehicle = new Vehicle(parts);
            vehicles.put(normalize(vehicle.plate), vehicle);
        }
    }

    private static void showRecord(Vehicle v) {
        System.out.println("Licence plate: " + v.plate);
        System.out.println("Make: " + v.make);
        System.out.println("Model: " + v.model);
        System.out.println("Year: " + v.year);
        System.out.println("Colour: " + v.colour);
        System.out.println("Owner: " + v.owner);
        System.out.println("Owner contact: " + v.contact);
        System.out.println("Registration status: " + v.registrationStatus);
        System.out.println("Licence expiry: " + v.licenceExpiry);
    }

    private static void launchGui() {
        JFrame frame = new JFrame("Smart Car Plate Identification System (VRIS)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(740, 550);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(22, 24, 22, 24));
        root.setBackground(new Color(244, 247, 252));

        JLabel heading = new JLabel("SMART CAR PLATE IDENTIFICATION SYSTEM");
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setForeground(new Color(19, 54, 101));
        JLabel subtitle = new JLabel("Vehicle and owner lookup | Fictional demonstration data");
        subtitle.setForeground(new Color(85, 91, 100));
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 16));
        center.setOpaque(false);
        JPanel search = new JPanel(new BorderLayout(10, 8));
        search.setOpaque(false);
        JLabel instruction = new JLabel("Enter registration number:");
        instruction.setFont(new Font("SansSerif", Font.BOLD, 14));
        JTextField plateField = new JTextField();
        plateField.setFont(new Font("SansSerif", Font.PLAIN, 17));
        plateField.setToolTipText("Example: ND 123 GP");
        JButton searchButton = new JButton("SEARCH VEHICLE");
        searchButton.setBackground(new Color(20, 83, 160));
        searchButton.setForeground(Color.WHITE);
        JPanel inputLine = new JPanel(new BorderLayout(10, 0));
        inputLine.setOpaque(false);
        inputLine.add(plateField, BorderLayout.CENTER);
        inputLine.add(searchButton, BorderLayout.EAST);
        search.add(instruction, BorderLayout.NORTH);
        search.add(inputLine, BorderLayout.CENTER);
        center.add(search, BorderLayout.NORTH);

        String[] labels = {"Registration number", "Vehicle make", "Vehicle model", "Year", "Colour",
                "Owner name", "Owner contact", "Registration status", "Licence expiry"};
        JPanel result = new JPanel(new GridLayout(labels.length, 2, 14, 8));
        result.setBackground(Color.WHITE);
        result.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 227, 238)),
                new EmptyBorder(18, 22, 18, 22)));
        JLabel[] values = new JLabel[labels.length];
        for (int i = 0; i < labels.length; i++) {
            JLabel title = new JLabel(labels[i]);
            title.setFont(new Font("SansSerif", Font.BOLD, 13));
            values[i] = new JLabel("—");
            result.add(title);
            result.add(values[i]);
        }
        center.add(result, BorderLayout.CENTER);
        JLabel status = new JLabel("Try: ND 123 GP, CA 456 WC, or KZN 789 EC");
        status.setForeground(new Color(68, 79, 96));
        center.add(status, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);

        Runnable searchAction = () -> {
            String query = normalize(plateField.getText());
            if (query.isEmpty()) {
                status.setText("Please enter a vehicle registration number.");
                return;
            }
            Vehicle v = vehicles.get(query);
            if (v == null) {
                for (JLabel value : values) value.setText("—");
                status.setText("No matching record found for: " + query);
                return;
            }
            String[] fields = {v.plate, v.make, v.model, v.year, v.colour,
                    v.owner, v.contact, v.registrationStatus, v.licenceExpiry};
            for (int i = 0; i < fields.length; i++) values[i].setText(fields[i]);
            status.setText("Matching vehicle record found.");
        };
        searchButton.addActionListener(event -> searchAction.run());
        plateField.addActionListener(event -> searchAction.run());
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            loadVehicles();
            if (args.length >= 2 && "--lookup".equals(args[0])) {
                Vehicle record = vehicles.get(normalize(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
                if (record == null) {
                    System.out.println("No matching vehicle record found.");
                    System.exit(2);
                }
                showRecord(record);
                return;
            }
            if (GraphicsEnvironment.isHeadless()) {
                System.err.println("No graphical desktop found. Try: java SmartCarPlateSystem --lookup ND 123 GP");
                System.exit(1);
            }
            SwingUtilities.invokeLater(SmartCarPlateSystem::launchGui);
        } catch (IOException e) {
            System.err.println("Unable to read vehicle data: " + e.getMessage());
            System.exit(1);
        }
    }
}
