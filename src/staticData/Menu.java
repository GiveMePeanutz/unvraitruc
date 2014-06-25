package staticData;

import java.util.HashMap;
import java.util.Map;

public final class Menu {

    private static String[][][] menus = {
                                      {
                                      { "User" },
                                      { "Display Users" },
                                      { "Add User" },
                                      { "Group",
                                      "Display Groups",
                                      "Add Groups" },
                                      { "Privilege",
                                      "Displays Privileges",
                                      "Add Privileges" }
                                      },
                                      {
                                      { "Course" },
                                      { "Display Courses" },
                                      { "Add a Course" }
                                      },
                                      {
                                      { "Teacher" },
                                      { "Display Teachers" },
                                      { "Add a Teacher" }
                                      },
                                      {
                                      { "Student" },
                                      { "Display Students" },
                                      { "Add a Student" }
                                      },
                                      {
                                      { "Warehouse Maintenance" }
                                      },
                                      {
                                      { "Data Analysis" }
                                      },
                                      {
                                      { "Profile" }
                                      }
                                      };

    public static String[][][] getMenus() {
        return menus;
    }

    public static String getMenuName( int path ) {

        return menus[path / 10000][( path - ( path / 10000 ) * 10000 ) / 100][( path - ( path / 100 ) * 100 )];
    }

    public static HashMap<Integer, String> list()
    {
        Map<Integer, String> mapMenus = new HashMap<Integer, String>();

        {
            mapMenus.put( path, getMenuName( path ) );
        }
        paths.add( map() );
        return paths;
    }

}
