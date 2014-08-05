package forms;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import utilities.UtilitiesForm;
import beans.Course;
import dao.CourseDao;
import dao.DAOException;

public final class CourseCreationForm {

    // Fields Name, have to be identical than fields name in JSPs
    private static final String NAME_FIELD        = "courseName";
    private static final String DESCRIPTION_FIELD = "courseDescription";
    private static final String YEAR_FIELD        = "courseYear";
    private static final String TEACHER_FIELD     = "teacher";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private CourseDao           courseDao;
    private UtilitiesForm       util              = new UtilitiesForm();

    public CourseCreationForm( CourseDao courseDao ) {
        this.courseDao = courseDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    // Method to create a course
    public Course createCourse( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String courseName = util.getFieldValue( request, NAME_FIELD );
        int courseYear = util.getIntValue( request, YEAR_FIELD );
        String courseDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        String teacher = util.getFieldValue( request, TEACHER_FIELD );

        Course course = new Course();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleCourseName( courseName, course );
        handleCourseDescription( courseDescription, course );
        handleCourseYear( courseYear, course );
        handleTeacher( teacher, course );

        try {

            // if no error, the course is created and saved in database, else an
            // error message is displayed
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

    // Method to modify a course
    public Course modifyCourse( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String courseName = util.getFieldValue( request, NAME_FIELD );
        int courseYear = util.getIntValue( request, YEAR_FIELD );
        String courseDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        String teacher = util.getFieldValue( request, TEACHER_FIELD );

        Course course = new Course();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleCourseName( courseName, course );
        handleCourseDescription( courseDescription, course );
        handleCourseYear( courseYear, course );
        handleTeacher( teacher, course );

        try {
            // if no error, the course is modified and saved in database, else
            // an error message is displayed
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

    // Course Name check : more than 3 characters
    private void courseNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    // Course Description check : more than 5 characters
    private void courseDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    // Course Year check : Has to be after 1980
    private void courseYearValidation( int courseYear ) throws FormValidationException {
        if ( courseYear != 0 ) {
            if ( courseYear < 1980 ) {
                throw new FormValidationException( "Course Year must be superior to the year 1980" );
            }
        } else {
            throw new FormValidationException( "Please enter a course year." );
        }
    }

    // Teacher choice check : can't be empty
    private void teacherValidation( String teacher ) throws FormValidationException {
        if ( teacher == null ) {

            throw new FormValidationException( "Please select one teacher." );
        }

    }

    // Add a message corresponding to the specific field to the error map.
    private void setError( String path, String message ) {
        errors.put( path, message );
    }

}