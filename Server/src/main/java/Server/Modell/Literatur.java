package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "literatur")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Literatur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String type;
    @Lob
    @Column( length = 100000 )
    private String Abstract;
    @Lob
    @Column( length = 100000 )
    private String title;
    @Lob
    @Column( length = 100000 )
    private String author;
    @Lob
    @Column( length = 100000 )
    private String year;
    @Lob
    @Column( length = 100000 )
    private String publisher;
    @Lob
    @Column( length = 100000 )
    private String pages;
    @Lob
    @Column( length = 100000 )
    private String volume;
    @Lob
    @Column( length = 100000 )
    private String number;
    @Lob
    @Column( length = 100000 )
    private String booktitle;
    @Lob
    @Column( length = 100000 )
    private String issn;
    @Lob
    @Column( length = 100000 )
    private String journal;
    @Lob
    @Column( length = 100000 )
    private String doi;
    @Lob
    @Column( length = 100000 )
    private String keywords;
    @Lob
    @Column( length = 100000 )
    private String adress;
    @Lob
    @Column( length = 100000 )
    private String series;
    @Lob
    @Column( length = 100000 )
    private String urldate;
    @Lob
    @Column( length = 100000 )
    private String file;
    @Lob
    @Column( length = 100000 )
    private String price;
    @Lob
    @Column( length = 100000 )
    private String isbn;
    @Lob
    @Column( length = 100000 )
    private String editor;
    @Lob
    @Column( length = 100000 )
    private String institution;
    @Lob
    @Column( length = 100000 )
    private String url;
    @Lob
    @Column( length = 100000 )
    private String edition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUrldate() {
        return urldate;
    }

    public void setUrldate(String urldate) {
        this.urldate = urldate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Override
    public String toString() {
        return "Literatur{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", Abstract='" + Abstract + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pages='" + pages + '\'' +
                ", volume='" + volume + '\'' +
                ", number='" + number + '\'' +
                ", booktitle='" + booktitle + '\'' +
                ", issn='" + issn + '\'' +
                ", journal='" + journal + '\'' +
                ", doi='" + doi + '\'' +
                ", keywords='" + keywords + '\'' +
                ", adress='" + adress + '\'' +
                ", series='" + series + '\'' +
                ", urldate='" + urldate + '\'' +
                ", file='" + file + '\'' +
                ", price='" + price + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editor='" + editor + '\'' +
                ", institution='" + institution + '\'' +
                ", url='" + url + '\'' +
                ", edition='" + edition + '\'' +
                '}';
    }
}
