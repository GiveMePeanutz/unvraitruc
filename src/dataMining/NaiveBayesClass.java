// NaiveBayes algorithm

package dataMining;

import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import beans.User;
import dao.NaiveBayesDao;
import dao.UserDao;

public class NaiveBayesClass {

    // Algorithm parameters
    private Attribute  sex;
    private Attribute  promotion;
    private Attribute  course;

    private Instances  isSet;

    // the classifier model
    private Classifier cModel;

    // Vector with all the attributes
    private FastVector fvAttributes;

    // likelihood table
    private double[]   fDistribution;

    public NaiveBayesClass( User user, UserDao userDao, NaiveBayesDao naiveBayesDao )
    {
        // Retrieves number of user, all the courses, all different promotions
        // and all the students.
        int numberOfUsers = naiveBayesDao.getUserCount();
        List<String> listCourse = naiveBayesDao.listCourse();
        List<String> listPromotion = naiveBayesDao.listPromotion();
        List<User> listStudent = userDao.listStudent();

        // Sex Attribute

        FastVector fvSexVal = new FastVector( 2 );
        fvSexVal.addElement( "Man" );
        fvSexVal.addElement( "Woman" );
        this.sex = new Attribute( "sex", fvSexVal );

        // Promotion Attribute

        FastVector fvPromotionVal = new FastVector( listPromotion.size() );
        for ( String promotionElt : listPromotion )
        {
            fvPromotionVal.addElement( promotionElt );
        }
        this.promotion = new Attribute( "promotion", fvPromotionVal );

        // Course Attribute

        FastVector fvCourseVal = new FastVector( listCourse.size() );
        for ( String courseElt : listCourse )
        {
            fvCourseVal.addElement( courseElt );
        }
        this.course = new Attribute( "course", fvCourseVal );

        // Attributes Vector

        this.fvAttributes = new FastVector( 3 );
        fvAttributes.addElement( sex );
        fvAttributes.addElement( promotion );
        fvAttributes.addElement( course );

        // Create an empty set
        this.isSet = new Instances( "Rel", fvAttributes, numberOfUsers );

        // Set class index : 2 = course
        isSet.setClassIndex( 2 );

        // Classifier choice : here, NaiveBaye
        this.cModel = (Classifier) new NaiveBayes();

        // Create the instance
        for ( User userList : listStudent )
        {

            if ( !userList.getUsername().equals( user.getUsername() ) )
            {
                for ( String course : userList.getCourseNames() )
                {
                    Instance i = new Instance( 3 );
                    if ( userList.getSex() == 1 )
                        i.setValue( (Attribute) fvAttributes.elementAt( 0 ), "Woman" );
                    else
                        i.setValue( (Attribute) fvAttributes.elementAt( 0 ), "Man" );

                    i.setValue( (Attribute) fvAttributes.elementAt( 1 ), userList.getPromotion() );
                    i.setValue( (Attribute) fvAttributes.elementAt( 2 ), course );

                    // add the instance
                    isSet.add( i );
                }

            }
        }

        // Build the classifier
        try {
            cModel.buildClassifier( isSet );
        } catch ( Exception e ) {
            System.out.println( "Error building the Classifier, please try again" );
        }
    }

    public String courseAdvice( User user ) throws Exception
    {
        String courseNameAdvised = "";
        // User on who the algorithm will work
        Instance iUser = new Instance( 3 );

        if ( user.getSex() == 1 )
            iUser.setValue( (Attribute) this.fvAttributes.elementAt( 0 ), "Woman" );
        else
            iUser.setValue( (Attribute) this.fvAttributes.elementAt( 0 ), "Man" );

        iUser.setValue( (Attribute) this.fvAttributes.elementAt( 1 ), user.getPromotion() );

        // in order to inherit from the set description
        iUser.setDataset( this.isSet );

        // Get the likelihood of each classes
        this.fDistribution = this.cModel.distributionForInstance( iUser );

        // Retrieving the biggest likelihood
        int max = indexMax( fDistribution );

        courseNameAdvised = this.course.value( max );

        return courseNameAdvised;
    }

    // Returns the biggest value of a table.
    public int indexMax( double[] T )
    {
        int max = 0;
        for ( int index = 0; index < T.length; index++ )
        {
            if ( T[index] > T[max] )
            {
                max = index;
            }

        }
        return max;
    }

    // Getter of the likelihood table
    public double[] getfDistribution() {
        return fDistribution;
    }

    // Returns courses
    public Attribute getCourse() {
        return course;
    }

}
