import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class GEDCOMParser {

    public static void main(String[] args) {
        String fileName = "/Users/jaydeepdobariya/Desktop/Spring Sem/CS 555 - Agile Methodologies/gedcom.ged"; // replace with actual file name

        Map<String, Individual> individualsMap = new TreeMap<>();
        Map<String, Family> familiesMap = new TreeMap<>();
        Map<String, ArrayList<String>> fileComments = new HashMap<>(){{
            put("HEAD", new ArrayList<>());
            put("TRLR", new ArrayList<>());
        }};

        List<String> commentSet = new ArrayList<>();
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
                }
                if (tokens[0].equals("0") && (!((tokens[1].equals("HEAD")||tokens[1].equals("TRLR")||tokens[1].equals("NOTE"))))) {
                    if (tokens.length >= 3 && tokens[2].equals("INDI")) {
                        currentIndividual = new Individual(tokens[1]);
//                        if(preTokens[1].equals("NOTE")){
//                            commentSet.add(String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length+1)));
//                            System.out.println(String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length)));
//                            currentIndividual.setComments(commentSet);
//                            commentSet.clear();
//                        }
                        individualsMap.put(tokens[1], currentIndividual);
                    } else if (tokens.length >= 3 && tokens[2].equals("FAM")) {
                        currentFamily = new Family(tokens[1]);
//                        if(preTokens[1].equals("NOTE")){
//                            commentSet.add(String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length+1)));
//                            currentFamily.setComments(commentSet);
//                            commentSet.clear();
//                        }
                        familiesMap.put(tokens[1], currentFamily);
                    }
                } else if (tokens[0].equals("1") || tokens[0].equals("2") || tokens[0].equals("0")) {
                    switch (tokens[1]) {
                        case "NAME":
                            if (currentIndividual != null) currentIndividual.setName(String.join(" ",Arrays.copyOfRange(tokens, 2, tokens.length)));
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
                            String dateStr = tokens[2] +" "+ tokens[3].charAt(0)+tokens[3].substring(1).toLowerCase() +" "+ tokens[4];

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

                            if (preTokens[1].equals("BIRT")) currentIndividual.setBirthday(LocalDate.parse(dateStr,formatter));

                            if (preTokens[1].equals("DEAT")) currentIndividual.setDeath(LocalDate.parse(dateStr,formatter));

                            if (preTokens[1].equals("DIV")) currentFamily.setDivorced(LocalDate.parse(dateStr,formatter));

                            if (preTokens[1].equals("MARR")) currentFamily.setMarried(LocalDate.parse(dateStr,formatter));
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

                        case "HEAD":
//                            if(preTokens[1].equals("NOTE")){
//                                String strCmnt = String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length+1));
//                                commentSet.add(strCmnt);
//                                for(String cmnt: commentSet){
//                                    fileComments.get("HEAD").add(cmnt);
//                                }
//                                commentSet.clear();
//                            }
                            break;

                        case "TRLR":
//                            if(preTokens[1].equals("NOTE")){
//                                commentSet.add(String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length+1)));
//                                for(String cmnt: commentSet){
//                                    fileComments.get("TRLR").add(cmnt);
//                                }
//                                commentSet.clear();
//                            }
                            break;

                        case "NOTE":
//                            commentSet.add(String.join(" ", Arrays.copyOfRange(preTokens, 1, tokens.length+1)));
                            break;

                    }
                }
                preTokens = tokens;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Individuals:");
        for (String iid : individualsMap.keySet()) {
            Individual indiv = individualsMap.get(iid);
            System.out.printf("ID = {%s}, Name = {%s}, Gender = {%s}, Birthday = {%s}, Age = {%d}, Alive = {%b}, Death = {%s}, Child = {%s}, Spouse = {%s}\n",
                    iid, indiv.getName(), indiv.getGender(), indiv.getBirthday().toString(), indiv.getAge(), indiv.isAlive(), indiv.getDeathDate().toString(), indiv.getChild(), indiv.getSpouse());
        }

        System.out.println("Family:");
        for (String fid : familiesMap.keySet()) {
            Family fam = familiesMap.get(fid);
            System.out.printf("ID = {%s}, Married = {%s}, Divorced = {%s}, Husband ID = {%s}, Husband Name = {%s}, Wife ID = {%s}, Wife Name = {%s}, Childern = {%s}\n",
                    fid, fam.getMarried().toString(), fam.getDivorced().toString(), fam.getHusband().getId(), fam.getHusband().getName(),fam.getWife().getId(), fam.getWife().getName(), fam.getChildern().toString());
        }

//        System.out.println("Head comments:");
//        for (String cmnt : fileComments.get("HEAD")) {
//            System.out.println(cmnt);
//        }
//        System.out.println("TRLR comments:");
//        for (String cmnt : fileComments.get("HEAD")) {
//            System.out.println(cmnt);
//        }
//        System.out.println("Individual comments:");
//        for (String iid : individualsMap.keySet()) {
//            Individual indiv = individualsMap.get(iid);
//            System.out.println(iid + ":");
//            for(String cmnt: indiv.getComments()){
//                System.out.println(cmnt);
//            }
//        }
//        System.out.println("Family comments:");
//        for (String fid : familiesMap.keySet()) {
//            Family fam = familiesMap.get(fid);
//            System.out.println(fid + ":");
//            for(String cmnt: fam.getComments()){
//                System.out.println(cmnt);
//            }
//        }
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
    private List<String> comments = new ArrayList<>();
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
    public LocalDate getBirthday(){
        return birthday;
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

    public List<String> getComments(){
        return comments;
    }
    public void setComments(List<String> comments){
        for(String cmnt : comments){
            comments.add(cmnt);
        }
    }
}

class Family {
    private String id = "NA";
    private LocalDate married = null;
    private LocalDate divorced = null;
    private Individual husband = null;
    private Individual wife;
    private List<String> childrenId = new ArrayList<>();
    private List<String> comments = new ArrayList<>();

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
        childrenId.add(ChildId);
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

    public List<String> getComments(){
        return comments;
    }
    public void setComments(List<String> comments){
        for(String cmnt : comments){
            comments.add(cmnt);
        }
    }
}