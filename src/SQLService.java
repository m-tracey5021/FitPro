
import java.sql.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;



public class SQLService {
	Connection connect = null;
	Statement statement = null;
	PreparedStatement prepStmt = null;
	String sqlQuery = null;
	ResultSet result = null;
	
	public SQLService() {
		connect();
	}
	
	public void connect() {
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/FitProDatabase?"
	    			+"user=root&password=3v3rChang1ng");
			
			
			System.out.println("Connected Successfully!");
		}catch(SQLException ex) {
			System.out.println(ex);
		}
		
	}
	

	/*
	 * public Cycle retrieveCycle(String Id) { Cycle retrievedCycle = new Cycle();
	 * try { statement = connect.createStatement(); sqlQuery =
	 * String.format("SELECT * FROM Cycles WHERE cycleId = '%s'", Id); result =
	 * statement.executeQuery(sqlQuery); result.first(); // should only produce one
	 * row retrievedCycle.setCycleName(result.getString(2));
	 * //System.out.println(retrievedCycle.getCycleName()); return retrievedCycle; }
	 * catch (SQLException ex) { System.out.println(ex); return retrievedCycle; }
	 * 
	 * 
	 * }
	 */
	public boolean isInserted(String Id, String IdType, String table) {
		try {
			
			statement = connect.createStatement();
			sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", table, IdType, Id);
			result = statement.executeQuery(sqlQuery);
			if (result.next() == false) {
				return false;
			}else {
				return true;
			}
			
		}catch (SQLException ex) {
			System.out.println(ex);
			return false;
		}
	}
	
	public <T> ResultSet getObjectByColumn(T value, String column, String table) {
		try {
			
			if (value.getClass().getName().equals("java.lang.String")) {
				statement = connect.createStatement(); 
				sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", table, column, value);
				result = statement.executeQuery(sqlQuery);
				return result;
			}else if (value.getClass().getName().contentEquals("java.lang.Integer")) {
				statement = connect.createStatement(); 
				sqlQuery = String.format("SELECT * FROM %s WHERE %s = %d", table, column, value);
				result = statement.executeQuery(sqlQuery);
				return result;
			}else if (value.getClass().getName().contentEquals("java.lang.Double")) {
				statement = connect.createStatement(); 
				sqlQuery = String.format("SELECT * FROM %s WHERE %s = %.3f", table, column, value);
				result = statement.executeQuery(sqlQuery);
				return result;
			}else if (value.getClass().getName().contentEquals("java.util.Date")) {
				statement = connect.createStatement(); 
				sqlQuery = String.format("SELECT * FROM %s WHERE %s = %tF", table, column, value);
				result = statement.executeQuery(sqlQuery);
				return result;
			}else {
				return result;
			}
			
		}catch (SQLException ex) {
			System.out.println(ex);
			return result;
		}
		
	}
	public ArrayList<Cycle> getCyclesByUser(String userId) {
		ArrayList<Cycle> cycleList = new ArrayList<Cycle>();
		try {
			
			ResultSet cyclesRS = getObjectByColumn(userId, "ownerId", "Cycles");
			
			while (cyclesRS.next()) {
				// have to get all the info for sets workouts etc before insantiating the cycle below
				String cycleId = cyclesRS.getString(1);
				//System.out.println("HERE");
				java.sql.Date startDate = cyclesRS.getDate(4);
				java.sql.Date endDate = cyclesRS.getDate(5);
				LocalDate convertedStart = null;
				LocalDate convertedEnd = null;
				if (startDate != null) {
					convertedStart = startDate.toLocalDate();
				}
				if (endDate != null) {
					convertedEnd = endDate.toLocalDate();
				}
				Cycle currentCycle = new Cycle(cyclesRS.getString(2), cyclesRS.getString(3), 
						convertedStart, convertedEnd, cyclesRS.getInt(6));
				currentCycle.setId(cycleId);
				
				ResultSet workoutsRS = getObjectByColumn(cycleId, "parentCycle", "Workouts");
				
				while (workoutsRS.next()) {
					
					String workoutId = workoutsRS.getString(1);
					
					
					java.sql.Date date = workoutsRS.getDate(3);
					java.sql.Time time = workoutsRS.getTime(4); 
					
					
					LocalDate convertedDate = null;
					LocalTime convertedTime = null;
					
					if (date != null) {
						convertedDate = date.toLocalDate();
					}
					if (time != null) {
						convertedTime = time.toLocalTime();
					}
					Workout currentWorkout = new Workout(convertedDate, convertedTime);
					currentWorkout.setId(workoutId);
					currentWorkout.setParentCycle(currentCycle);
					
					/*
					 * as the function trickles down, add each workout/movement etc to the item below, as a parent, then
					 * at the end of the function, after each while loop, add each workout/movement etc
					 * to the item above, to create the lists of owned workouts etc
					 */
					
					ResultSet movementsRS = getObjectByColumn(workoutId, "parentWorkout", "Movements");
					
					while(movementsRS.next()) {
						
						String movementId = movementsRS.getString(1);
						Movement currentMovement = new Movement(movementsRS.getString(3), movementsRS.getDouble(4));
						currentMovement.setId(movementId);
						currentMovement.setParentWorkout(currentWorkout);
						
						ResultSet setsRS = getObjectByColumn(movementId, "parentMovement", "Sets");
						
						while(setsRS.next()) {
							
							String setId = setsRS.getString(1);
							Set currentSet = new Set(setsRS.getInt(3), setsRS.getDouble(4));
							currentSet.setId(setId);
							currentSet.setParentMovement(currentMovement);
							currentMovement.addSet(currentSet);
						}
						currentWorkout.addMovement(currentMovement);
					}
					currentCycle.addWorkout(currentWorkout);
				}
				
				cycleList.add(currentCycle);
			}
			return cycleList;
		}catch (SQLException ex) {
			System.out.println(ex);
			return cycleList;
		}
	}
	
	
	
	public <T extends CycleComponent> void upsertObject(Class<T> objectClass, T fitProObject) {
		if (objectClass.getName() == "User") {
			if (isInserted(fitProObject.getId(), "userId", "Users")) {
				// update the database
			}else {
				// insert into the database
			}
		}
		if (objectClass.getName() == "Cycle") {
			if (isInserted(fitProObject.getId(), "cycleId", "Cycles")) {
				// update the database
			}else {
				// insert into the database
			}
		}
		if (objectClass.getName() == "Workout") {
			if (isInserted(fitProObject.getId(), "workoutId", "Workouts")) {
				// update the database
			}else {
				// insert into the database
			}
		}
		if (objectClass.getName() == "Movement") {
			if (isInserted(fitProObject.getId(), "movementId", "Movements")) {
				// update the database
			}else {
				// insert into the database
			}
		}
		if (objectClass.getName() == "Set") {
			if (isInserted(fitProObject.getId(), "setId", "Sets")) {
				// update the database
			}else {
				// insert into the database
			}
		}
		
	}
	
	public void upsertUser(User u) {
		try {
			if (isInserted(u.getId(), "userId", "Users")) {
				String updateSQL = "UPDATE Users Set userName = ?, userPass = ?, userEmail = ?, membershipType = ? WHERE userId = ?";
				PreparedStatement updateStmt = connect.prepareStatement(updateSQL);
				updateStmt.setString(1, u.getUserName());
				updateStmt.setString(2, u.getUserPass());
				updateStmt.setString(3, u.getEmail());
				updateStmt.setInt(4, u.getMembershipType());
				updateStmt.setString(5, u.getId());
				updateStmt.executeUpdate();
			}else {
				String insertSQL = "INSERT INTO Users (userId, userName, userPass, userEmail, membershipType)"
						+ " VALUES (?, ?, ?, ?, ?)";
				PreparedStatement insertStmt = connect.prepareStatement(insertSQL);
				insertStmt.setString(1, u.getId());
				insertStmt.setString(2, u.getUserName());
				insertStmt.setString(3, u.getUserPass());
				insertStmt.setString(4, u.getEmail());
				insertStmt.setInt(5, u.getMembershipType());
				insertStmt.executeUpdate();
				
			}
		}catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	
	public void upsertCycle(Cycle c) {
		try {
			if (isInserted(c.getId(), "cycleId", "Cycles")) {
				String updateSQL = "UPDATE Cycles SET ownerId = ?, cycleName = ?, startDate = ?, endDate = ?, totalWorkouts = ? WHERE cycleId = ?";
				PreparedStatement updateStmt = connect.prepareStatement(updateSQL);
				updateStmt.setString(1, c.getOwnerId());
				updateStmt.setString(2, c.getCycleName());
				if (c.getHasDates()) {
					updateStmt.setDate(3, java.sql.Date.valueOf(c.getDates()[0]));
					updateStmt.setDate(4, java.sql.Date.valueOf(c.getDates()[1]));
				}else {
					updateStmt.setDate(3, null);
					updateStmt.setDate(4, null);
				}
				updateStmt.setInt(5, c.getTotalWorkouts());
				updateStmt.setString(6, c.getId());
				updateStmt.executeUpdate();
			}else {
				String insertSQL = "INSERT INTO Cycles (cycleId, ownerId, cycleName, startDate, endDate, totalWorkouts) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement insertStmt = connect.prepareStatement(insertSQL);
				insertStmt.setString(1, c.getId());
				insertStmt.setString(2, c.getOwnerId());
				insertStmt.setString(3, c.getCycleName());
				if (c.getHasDates()) {
					insertStmt.setDate(4, java.sql.Date.valueOf(c.getDates()[0]));
					insertStmt.setDate(5, java.sql.Date.valueOf(c.getDates()[1]));
				}else {
					insertStmt.setDate(4, null);
					insertStmt.setDate(5, null);
				}
				
				insertStmt.setInt(6, c.getTotalWorkouts());
				insertStmt.executeUpdate();
			}
			for (Workout w : c.getWorkouts()) {
				upsertWorkout(w);
			}
			
			
			
		}catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	
	public void upsertWorkout(Workout w) {
		try {
			if (isInserted(w.getId(), "workoutId", "Workouts")) {
				String updateSQL = "UPDATE Workouts SET parentCycle = ?, workoutDate = ?, workoutTime = ? WHERE workoutId = ?";
				PreparedStatement updateStmt = connect.prepareStatement(updateSQL);
				//System.out.println("Prep stmt: " + updateStmt.hashCode());
				updateStmt.setString(1, w.getParentCycle().getId());
				if (w.getDate() != null) {
					updateStmt.setDate(2, java.sql.Date.valueOf(w.getDate()));
					//System.out.println(w.getDate() + " IN UPDATE");
					//System.out.println(w);
				}else {
					updateStmt.setDate(2, null);
				}
				if (w.getTime() != null) {
					updateStmt.setTime(3, java.sql.Time.valueOf(w.getTime()));
				}else {
					updateStmt.setTime(3, null);
				}
				updateStmt.setString(4, w.getId());
				updateStmt.executeUpdate();
				
			}else {
				String insertSQL = "INSERT INTO Workouts (workoutId, parentCycle, workoutDate, workoutTime) "
						+ "VALUES (?, ?, ?, ?)";
				PreparedStatement insertStmt = connect.prepareStatement(insertSQL);
				insertStmt.setString(1, w.getId());
				insertStmt.setString(2,  w.getParentCycle().getId());
				if (w.getDate() != null) {
					insertStmt.setDate(3, java.sql.Date.valueOf(w.getDate()));
				}else {
					insertStmt.setDate(3, null);
				}
				if (w.getTime() != null) {
					insertStmt.setTime(4, java.sql.Time.valueOf(w.getTime()));
				}else {
					insertStmt.setTime(4, null);
				}
				insertStmt.executeUpdate();
			}
			for (Movement m : w.getMovements()) {
				upsertMovement(m);
			}
		}catch (SQLException ex) {
			System.out.println(ex);
		}
		
	}
	
	public void upsertMovement(Movement m) {
		try {
			if (isInserted(m.getId(), "movementId", "Movements")) {
				String updateSQL = "UPDATE Movements SET parentWorkout = ?, movementName = ?, current1RM = ? WHERE movementId = ?";
				PreparedStatement updateStmt = connect.prepareStatement(updateSQL);
				updateStmt.setString(1, m.getParentWorkout().getId());
				updateStmt.setString(2, m.getName());
				updateStmt.setDouble(3, m.getCurrent1RM());
				updateStmt.setString(4, m.getId());
				updateStmt.executeUpdate();
			}else {
				String insertSQL = "INSERT INTO Movements (movementId, parentWorkout, movementName, current1RM) "
						+ "VALUES (?, ?, ?, ?)";
				PreparedStatement insertStmt = connect.prepareStatement(insertSQL);
				insertStmt.setString(1, m.getId());
				insertStmt.setString(2, m.getParentWorkout().getId());
				insertStmt.setString(3, m.getName());
				insertStmt.setDouble(4, m.getCurrent1RM());
				insertStmt.executeUpdate();
			}
			for (Set s : m.getSets()) {
				upsertSet(s);
			}
				
		}catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	
	public void upsertSet(Set s) {
		try {
			if (isInserted(s.getId(), "setId", "Sets")) {
				String updateSQL = "UPDATE Sets SET parentMovement = ?, reps = ?, weight = ? WHERE setId = ?";
				PreparedStatement updateStmt = connect.prepareStatement(updateSQL);
				updateStmt.setString(1, s.getParentMovement().getId());
				updateStmt.setInt(2,  s.getReps());
				updateStmt.setDouble(3, s.getWeight());
				updateStmt.setString(4, s.getId());
				updateStmt.executeUpdate();
			}else {
				String insertSQL = "INSERT INTO Sets (setId, parentMovement, reps, weight) VALUES (?, ?, ?, ?)";
				PreparedStatement insertStmt = connect.prepareStatement(insertSQL);
				insertStmt.setString(1, s.getId());
				insertStmt.setString(2, s.getParentMovement().getId());
				insertStmt.setInt(3, s.getReps());
				insertStmt.setDouble(4, s.getWeight());
				insertStmt.executeUpdate();
			}
				
		}catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	
}
