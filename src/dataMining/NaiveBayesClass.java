// NaiveBayes algorithm

package dataMining;

import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import beans.User;
import dao.NaiveBayesDao;
import dao.UserDao;

public class NaiveBayesClass {

    private Attribute  sex;
    private Attribute  promotion;
    private Attribute  course;
    private Instances  isSet;
    private Classifier cModel;
    private FastVector fvAttributes;
    private double[]   fDistribution;

    public NaiveBayesClass( User user, UserDao userDao, NaiveBayesDao naiveBayesDao )
    {
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

        // Attribute Vector

        this.fvAttributes = new FastVector( 3 );
        fvAttributes.addElement( sex );
        fvAttributes.addElement( promotion );
        fvAttributes.addElement( course );

        // Create an empty set
        this.isSet = new Instances( "Rel", fvAttributes, numberOfUsers );

        // Set class index
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

    public String classifierTest() throws Exception
    {
        Evaluation eTest = new Evaluation( this.isSet );
        eTest.evaluateModel( this.cModel, this.isSet );
        String strSummary = eTest.toSummaryString();
        return strSummary;
    }

    public String courseAdvice( User user ) throws Exception
    {
        String courseNameAdvised = "";
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
        int max = indexMax( fDistribution );

        courseNameAdvised = this.course.value( max );

        return courseNameAdvised;
    }

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

    public double[] getfDistribution() {
        return fDistribution;
    }

    public Attribute getSex() {
        return sex;
    }

    public Attribute getPromotion() {
        return promotion;
    }

    public Attribute getCourse() {
        return course;
    }

    public Instances getIsSet() {
        return isSet;
    }

    public Classifier getcModel() {
        return cModel;
    }

    public FastVector getFvAttributes() {
        return fvAttributes;
    }

}
