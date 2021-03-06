package staticData;

import java.util.LinkedHashMap;

public final class Menu {

    // hard coded privileges' menu
    private static String[][][] menus =
                                      {
                                      {
                                      { "User", "Delete User", "Modify User" },
                                      { "Display Users" },
                                      { "Add User" },
                                      { "Group",
                                      "Display Groups",
                                      "Add Group"
                                      },
                                      { "Privilege",
                                      "Display Privileges",
                                      "Add Privilege"
                                      }
                                      },
                                      {
                                      { "Course", "Delete Course", "Modify Course", "Course Subscription" },
                                      { "Display Courses" },
                                      { "Add Course" }
                                      },
                                      {
                                      { "Teacher", "Delete Teacher", "Modify Teacher" },
                                      { "Display Teachers" },
                                      { "Add Teacher" },
                                      },
                                      {
                                      { "Student", "Delete Student", "Modify Student" },
                                      { "Display Students" },
                                      { "Add Student" },

                                      },
                                      {
                                      { "Data Warehouse" },
                                      },
                                      {
                                      { "Data Analysis" },
                                      { "Naive Bayes" },
                                      { "KMeans" }
                                      },
                                      {
                                      { "Profile" }
                                      }
                                      };

    public static String[][][] getMenus() {
        return menus;
    }

    // returns the name of the menu corresponding to the path in parameter.
    public static String getMenuName( int path ) throws ArrayIndexOutOfBoundsException {
        try {
            return menus[path / 10000][( path - ( path / 10000 ) * 10000 ) / 100][( path - ( path / 100 ) * 100 )];
        } catch ( Exception e ) {
            return "Path invalid";
        }
    }

    // returns a map of all menu names, key is the path, value is the name
    public static LinkedHashMap<Integer, String> list()
    {
        LinkedHashMap<Integer, String> mapMenus = new LinkedHashMap<Integer, String>();
        for ( int i = 0; i < menus.length; i++ )
        {

            for ( int j = 0; j < menus[i].length; j++ )
            {
                for ( int k = 0; k < menus[i][j].length; k++ )
                {

                    mapMenus.put( getPath( i, j, k ), getMenuName( getPath( i, j, k ) ) );
                }
            }
        }

        return mapMenus;
    }

    // returns the path of the menu corresponding to the name in parameter.
    public static int getPath( int firstmenu, int submenu, int subsubmenu ) {

        return ( firstmenu * 10000 + submenu * 100 + subsubmenu );
    }

}
