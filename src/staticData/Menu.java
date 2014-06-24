package staticData;

public final class Menu {
	
	private static String[][][] menus ={ 
		{ 
			{"User"},
				{"Display Users"},
				{"Add User"},
				{"Group",
					"Display Groups",
					"Add Groups"}, 
				{"Privilege",
					"Displays Privileges",
					"Add Privileges"} 
		}, 
		{ 
			{"Course"},
				{"Display Courses"}, 
				{"Add a Course"} 
		}, 
		{ 
			{"Teacher"},
				{"Display Teachers"}, 
				{"Add a Teacher"} 
		}, 
		{ 
			{"Student"},
				{"Display Students"}, 
				{"Add a Student"} 
		},
		{ 
			{"Warehouse Maintenance"} 
		},
		{ 
			{"Data Analysis"} 
		},
		{ 
			{"Profile"} 
		}
	};

	public static String[][][] getMenus() {
		return menus;
	}
	
	public static String getMenuName(int path){
		
		
		return null;
	}

			
			

}
