package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by lubuntu on 8/20/16.
 */
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String firstName;
    public String lastName;
    public String company;
}
