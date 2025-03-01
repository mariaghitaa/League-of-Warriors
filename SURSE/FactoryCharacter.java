package tema;

public class FactoryCharacter {
    public static Character createCharacter(String profession, String name, int experience, int level){
        switch(profession.toLowerCase()){
            case "mage":
                return new Mage(name, experience, level);
            case "warrior":
                return new Warrior(name, experience, level);
            case "rogue":
                return new Rogue(name, experience, level);
            default:
                throw new IllegalArgumentException("Invalid character profession: " + profession); 
        }   
    }
}
