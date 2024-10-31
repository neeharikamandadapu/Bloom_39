package asuHelloWorldJavaFX;

public class HelpArticle {
    private String title;
    private String description;
    private String keywords;
    private String group;
    private String body; // Body of the article
    private String level; // Level of the article
    private String links;
    private long id;// Links associated with the article

    // Constructor
    public HelpArticle(String title, String description, String keywords, String group, String body, String level, String links, long id) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.group = group;
        this.body = body;
        this.level = level;
        this.links = links;
        this.id = id;   
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getKeywords() { return keywords; }
    public String getGroup() { return group; }
    public String getBody() { return body; }
    public String getLevel() { return level; }
    public String getLinks() { return links; }
    public long getId() {return id;}
}