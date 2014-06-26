package staticData;

import java.util.HashMap;

public final class Menu {

    private static String[][][] menus =
                                      {
                                      {
                                      { "User" },
                                      { "Display Users" },
                                      { "Add User" },
                                      { "Group",
                                      "Display Groups",
                                      "Add Groups"
                                      },
                                      { "Privilege",
                                      "Displays Privileges",
                                      "Add Privileges"
                                      }
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
        HashMap<Integer, String> mapMenus = new HashMap<Integer, String>();
        int i = 0;
        while ( menus[i][0][0] != null )
        {
            int j = 0;
            while ( menus[i][j][0] != null )
            {
                int k = 0;
                while ( menus[i][j][k] != null )
                {
                    mapMenus.put( getPath( i, j, k ), getMenuName( getPath( i, j, k ) ) );
                    k = k + 1;
                }
                j = j + 1;
            }
            i = i + 1;
        }

        return mapMenus;
    }

    public static int getPath( int firstmenu, int submenu, int subsubmenu ) {

        return ( firstmenu * 10000 + submenu * 100 + subsubmenu );
    }

}
