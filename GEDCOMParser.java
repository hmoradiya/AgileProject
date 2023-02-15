import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;


public class GEDCOMParser {

    public static void main(String[] args) {
        String fileName = "/Users/jaydeepdobariya/Desktop/Spring Sem/CS 555 - Agile Methodologies/gedcom.ged"; // replace with actual file name

        Map<String, Individual> individualsMap = new TreeMap<>();
        Map<String, Family> familiesMap = new TreeMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Individual currentIndividual = null;
            Family currentFamily = null;
            String[] preTokens = null;
            boolean preTokenflag = true;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (preTokenflag) {
                    preTokens = tokens;
                    preTokenflag = false;
                } else preTokenflag = true;

                if (tokens[0].equals("0")) {
                    if (tokens.length >= 3 && tokens[2].equals("INDI")) {
                        currentIndividual = new Individual(tokens[1]);
                        individualsMap.put(tokens[1], currentIndividual);
                    } else if (tokens.length >= 3 && tokens[2].equals("FAM")) {
                        currentFamily = new Family(tokens[1]);
                        familiesMap.put(tokens[1], currentFamily);
                    }
                } else if (tokens[0].equals("1")) {
                    switch (tokens[1]) {
                        case "NAME":
                            if (currentIndividual != null) currentIndividual.setName(tokens[2]);
                            break;

                        case "HUSB":
                            if (currentFamily != null) currentFamily.setHusband(individualsMap.get(tokens[2]));
                            break;

                        case "WIFE":
                            if (currentFamily != null) currentFamily.setWife(individualsMap.get(tokens[2]));
                            break;

                        case "CHIL":
                            if (currentFamily != null) currentFamily.addChildern(tokens[2]);
                            break;

                        case "DATE":
                            String dateStr = tokens[2] +" "+ tokens[3] +" "+ tokens[4];
                            if (preTokens[1].equals("BIRT")) currentIndividual.setBirthday(LocalDate.parse(dateStr));

                            if (preTokens[1].equals("DEAT")) currentIndividual.setDeath(LocalDate.parse(dateStr));

                            if (preTokens[1].equals("DIV")) currentFamily.setDivorced(LocalDate.parse(dateStr));

                            if (preTokens[1].equals("MARR")) currentFamily.setMarried(LocalDate.parse(dateStr));
                            break;

                        case "SEX":
                            String gen = tokens[2].toLowerCase();
                            if (gen.equals("m") || gen.equals("male"))
                                currentIndividual.setGender("M");

                            if (gen.equals("f") || gen.equals("female"))
                                currentIndividual.setGender("F");
                            break;

                        case "FAMC":
                            currentIndividual.setChild(tokens[2]);
                            break;

                        case "FAMS":
                            currentIndividual.setSpouse(tokens[2]);
                            break;

                        default:
                            System.out.println("Something went wrong!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Individuals:");
        for (String iid : individualsMap.keySet()) {
            Individual indiv = individualsMap.get(iid);
            System.out.printf("ID = {%s}, Name = {%s}, Gender = {%s}, Birthday = {%s}, Age = {%d}, Alive = {%b}, Death = {%s}, Child = {%s}, Spouse = {%s}",
                    iid, indiv.getName(), indiv.getGender(), indiv.getBirthday().toString(), indiv.getAge(), indiv.isAlive(), indiv.getDeathDate().toString(), indiv.getChild(), indiv.getSpouse());
        }

        for (String fid : familiesMap.keySet()) {
            Family fam = familiesMap.get(fid);
            System.out.printf("ID = {%s}, Married = {%s}, Divorced = {%s}, Husband ID = {%s}, Husband Name = {%s}, Wife ID = {%s}, Wife Name = {%s}, Childern = {%s}",
                    fid, fam.getMarried().toString(), fam.getDivorced().toString(), fam.getHusband().getId(), fam.getHusband().getName(),fam.getWife().getId(), fam.getWife().getName(), fam.getChildern().toString());
        }
    }
}

class Individual {
    private String id = "NA";
    private String name = "NA";
    private String gender = "NA";
    private LocalDate birthday = null;
    private int age = -1;
    private boolean alive = true;
    private LocalDate death = null;
    private String isSpouse = "NA";
    private String isChild = "NA";
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

    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public Object getBirthday(){
        if(birthday != null)
            return birthday;
        else return "NA";
    }
    public void setBirthday(LocalDate dob){
        this.birthday = dob;
    }
    private int calcAge(LocalDate dob){
        LocalDate birthDate = LocalDate.parse(dob.toString());
        LocalDate currDate = LocalDate.now();
        this.age = Period.between(birthday, currDate).getYears();
        return age;
    }
    public int getAge(){
        return calcAge(birthday);
    }

    public boolean isAlive(){
        return alive;
    }

    public Object getDeathDate() {
        if(death != null)
            return death;
        else return "NA";
    }

    public void setDeath(LocalDate death){
        this.death = death;
        this.alive = false;
    }

    public String getSpouse(){
        return isSpouse;
    }

    public void setSpouse(String spouse){
        this.isSpouse = spouse;
    }

    public String getChild(){
        return isChild;
    }

    public void setChild(String child){
        this.isChild = child;
    }
}

class Family {
    private String id = "NA";
    private LocalDate married = null;
    private LocalDate divorced = null;
    private Individual husband = null;
    private Individual wife;
    private List<String> childrenId = new ArrayList<>();

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

    public void addChildern(String ChildId) {
        childrenId.add(id);
    }

    public List getChildern(){
        return childrenId;
    }

    public Object getMarried(){
        if(married != null)
            return married;
        else return "NA";
    }
    public void setMarried(LocalDate married){
        this.married = married;
    }

    public Object getDivorced(){
        if(divorced != null)
            return divorced;
        else return "NA";
    }
    public void setDivorced(LocalDate divorced){
        this.divorced = divorced;
    }
}