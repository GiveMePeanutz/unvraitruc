package forms;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.Course;
import dao.CourseDao;
import dao.DAOException;

public final class CourseCreationForm {

    private static final String NAME_FIELD        = "courseName";
    private static final String DESCRIPTION_FIELD = "courseDescription";
    private static final String YEAR_FIELD        = "courseYear";
    private static final String TEACHER_FIELD     = "teacher";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private CourseDao           courseDao;

    public CourseCreationForm( CourseDao courseDao ) {
        this.courseDao = courseDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    public Course createCourse( HttpServletRequest request, String path ) throws ParseException {

        String courseName = getFieldValue( request, NAME_FIELD );
        int courseYear = getIntValue( request, YEAR_FIELD );
        String courseDescription = getFieldValue( request, DESCRIPTION_FIELD );
        String teacher = getFieldValue( request, TEACHER_FIELD );

        Course course = new Course();
        handleCourseName( courseName, course );
        handleCourseDescription( courseDescription, course );
        handleCourseYear( courseYear, course );
        handleTeacher( teacher, course );

        try {
            if ( errors.isEmpty() ) {
                courseDao.create( course, teacher );
                result = "Course creation succeed";
            } else {
                result = "Course creation failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Course creation failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return course;
    }

    public Course modifyCourse( HttpServletRequest request, String path ) throws ParseException {

        String courseName = getFieldValue( request, NAME_FIELD );
        int courseYear = getIntValue( request, YEAR_FIELD );
        String courseDescription = getFieldValue( request, DESCRIPTION_FIELD );
        String teacher = getFieldValue( request, TEACHER_FIELD );

        Course course = new Course();
        handleCourseName( courseName, course );
        handleCourseDescription( courseDescription, course );
        handleCourseYear( courseYear, course );
        handleTeacher( teacher, course );

        try {
            if ( errors.isEmpty() ) {
                courseDao.modify( course );
                result = "Course modification succeed";
            } else {
                result = "Course modification failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Course modification failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return course;
    }

    private void handleCourseName( String courseName, Course course ) {
        try {
            courseNameValidation( courseName );
        } catch ( FormValidationException e ) {
            setError( NAME_FIELD, e.getMessage() );
        }
        course.setCourseName( courseName );
    }

    private void handleCourseDescription( String courseDescription, Course course ) {
        try {
            courseDescriptionValidation( courseDescription );
        } catch ( FormValidationException e ) {
            setError( DESCRIPTION_FIELD, e.getMessage() );
        }
        course.setCourseDescription( courseDescription );
    }

    private void handleCourseYear( int courseYear, Course course ) {
        try {
            courseYearValidation( courseYear );
        } catch ( FormValidationException e ) {
            setError( YEAR_FIELD, e.getMessage() );
        }
        course.setCourseYear( courseYear );

    }

    private void handleTeacher( String teacher, Course course ) {
        try {
            teacherValidation( teacher );
        } catch ( FormValidationException e ) {
            setError( TEACHER_FIELD, e.getMessage() );
        }
        course.setTeacher( teacher );

    }

    private void courseNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    private void courseDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    private void courseYearValidation( int courseYear ) throws FormValidationException {
        if ( courseYear != 0 ) {
            if ( courseYear < 1900 ) {
                throw new FormValidationException( "Course Year must be superior to the year 1900" );
            }
        } else {
            throw new FormValidationException( "Please enter a course year." );
        }
    }

    private void teacherValidation( String teacher ) throws FormValidationException {
        if ( teacher == null ) {

            throw new FormValidationException( "Please select one teacher." );
        }

    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des errors.
     */
    private void setError( String path, String message ) {
        errors.put( path, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

    private static int getIntValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        int valueInt = Integer.parseInt( value );
        if ( valueInt == 0 ) {
            return 0;
        } else {
            return valueInt;
        }
    }

}