package org.example;

public class CustomObjectGetAndSetForAll {
    private String id; // Change "Id" to "id"
    private String Name;
    private String book_name__c;
    private Attributes attributes;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBook_name__c() {
        return book_name__c;
    }

    public void setBook_name__c(String book_name__c) {
        this.book_name__c = book_name__c;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "CustomObjectGetAndSet{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", book_name__c='" + book_name__c + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    // Inner class for 'attributes'
    public static class Attributes {
        private String type;
        private String url;

        // Getters and setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Attributes{" +
                    "type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}

