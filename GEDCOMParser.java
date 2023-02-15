import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GEDCOMParser {

    public static void main(String[] args) {
        String fileName = "/home/krinal/Desktop/Agile/Project01.ged"; // replace with actual file name
        List<Individual> individuals = new ArrayList<>();
        List<Family> families = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Individual currentIndividual = null;
            Family currentFamily = null;
            Map<String, Individual> individualsMap = new HashMap<>();
            Map<String, Family> familiesMap = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");

                if (tokens[0].equals("0")) {
                    if (tokens.length >= 3 && tokens[2].equals("INDI")) {
                        currentIndividual = new Individual(tokens[1]);
                        individualsMap.put(tokens[1], currentIndividual);
                        individuals.add(currentIndividual);
                    } else if (tokens.length >= 3 && tokens[2].equals("FAM")) {
                        currentFamily = new Family(tokens[1]);
                        familiesMap.put(tokens[1], currentFamily);
                        families.add(currentFamily);
                    }
                } else if (tokens[0].equals("1")) {
                    switch (tokens[1]) {
                        case "NAME":
                            if (currentIndividual != null) {
                                currentIndividual.setName(tokens[2]);
                            }
                            break;
                        case "HUSB":
                            if (currentFamily != null) {
                                currentFamily.setHusband(individualsMap.get(tokens[2]));
                            }
                            break;
                        case "WIFE":
                            if (currentFamily != null) {
                                currentFamily.setWife(individualsMap.get(tokens[2]));
                            }
                            break;
                        // case "CHIL":
                        // if (currentFamily != null) {
                        // currentFamily.addChild(individualsMap.get(tokens[2]));
                        // }
                        // break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Individuals:");
        for (Individual individual : individuals) {
            System.out.println(individual.getId() + " " + individual.getName());
        }

        System.out.println("Families:");
        for (Family family : families) {
            System.out.println(family.getId() + " " + family.getHusband().getName() + " and "
                    + family.getWife().getName());
        }
    }
}

class Individual {
    private String id;
    private String name;

    public Individual(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Family {
    private String id;
    private Individual husband;
    private Individual wife;
    // private List<Individual> children = new ArrayList<>();

    public Family(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Individual getHusband() {
        return husband;
    }

    public void setHusband(Individual husband) {
        this.husband = husband;
    }

    public Individual getWife() {
        return wife;
    }

    public void setWife(Individual wife) {
        this.wife = wife;
    }
}