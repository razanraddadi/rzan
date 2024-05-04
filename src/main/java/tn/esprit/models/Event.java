package tn.esprit.models;

public class Event {

        private int id;
        private String name;
        private String description;
        private int capacity;
        private int reserved;
        private String date;
        private String end;

        private double prix;
        private String imageFile;
    private String destination;

    public Event(int id,
                 String name,
                 double prix){
        this.id = id;
        this.name = name;



        this.prix = prix;

    }
        public Event(int id, String name, String description, int capacity, int reserved, String date, String end, double prix, String imageFile, String destination) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.capacity = capacity;
            this.reserved = reserved;
            this.date = date;
            this.end = end;


            this.prix = prix;
            this.imageFile = imageFile;
            this.destination = destination;

        }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }



    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }




    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", destination='" + destination + '\'' +
                ", capacity=" + capacity +
                ", reserved=" + reserved +
                ", date='" + date + '\'' +
                ", end='" + end + '\'' +
                ", prix=" + prix +
                ", imageFile='" + imageFile + '\'' +
                '}';
    }
}
