package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ServiceFunctionalityException;

import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.Tables;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivityDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ActivitylinksDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.ProjectDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.daos.UseractivitiesDao;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activitylinks;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Useractivities;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;
import org.jooq.exception.DataAccessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityServiceImpl implements ActivityService {

    private final DSLContext create;
    private final ProjectDao projectDao;
    private final ActivityDao activityDao;
    private final UseractivitiesDao userActivitiesDao;
    private final ActivitylinksDao activitylinksDao;

    private final int projectId;

    public ActivityServiceImpl(DSLContext create, int projectId) {
        this.create = create;
        this.projectId = projectId;
        projectDao = new ProjectDao( create.configuration() );
        activityDao = new ActivityDao( create.configuration() );
        userActivitiesDao = new UseractivitiesDao (create.configuration());
        activitylinksDao = new ActivitylinksDao( create.configuration() );
    }

    @Override
    public Project getProject() {
        try {
            return projectDao.fetchOneById(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project with id " + projectId, e);
        }
    }

    @Override
    public List<Activity> getActivities() {
        try {
            return activityDao.fetchByProjectId(projectId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get all activities", e);
        }
    }

    @Override
    public List<Activity> getActivities(List<Integer> activityIds) {
        try {
            return create.select()
                    .from(Tables.ACTIVITY)
                    .where(Tables.ACTIVITY.PROJECT_ID.eq(projectId))
                    .and(Tables.ACTIVITY.ID.in(activityIds))
                    .fetchInto(Activity.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get specified activities", e);
        }
    }

    @Override
    public Activity getActivity(int activityId) {
        try {
            return activityDao.fetchOneById(activityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get activity with id " + activityId, e);
        }
    }

    @Override
    public void deleteActivity(int activityId) {

        checkProjectNotCompleted( "Project to delete activity from is completed" );

        try {
            activityDao.deleteById(activityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to delete activity with id " + activityId, e);
        }
    }

    @Override
	public void addActivity(Activity activity) {
	
	    checkProjectNotCompleted( "Project to add activity to is completed" );
	
	    try {
	        activityDao.insert(activity);
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new activity", e);
	    }
	}

	@Override
    public void updateActivity(Activity activity) {
        try {
            activityDao.update(activity);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to update existing activity", e);
        }
    }

    @Override
    public void addDependency(int fromActivityId, int toActivityId) {

        checkProjectNotCompleted( "Project to add activity dependency is completed" );
        checkCircularDependency(fromActivityId, toActivityId, "Project will have circular activities if these were added");
        
        Activitylinks link = new Activitylinks( null, fromActivityId, toActivityId );

        try {
            activitylinksDao.insert(link);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to add a new dependency from " + fromActivityId + " to " + toActivityId , e);
        }
    }

    @Override
    public void deleteDependency(int fromActivityId, int toActivityId) {

        checkProjectNotCompleted( "Project to delete activity dependency is completed" );
        

        try {
            create.deleteFrom(Tables.ACTIVITYLINKS)
                    .where(Tables.ACTIVITYLINKS.FROM_ACTIVITY_ID.eq(fromActivityId))
                    .and(Tables.ACTIVITYLINKS.TO_ACTIVITY_ID.eq(toActivityId))
                    .execute();
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to delete a dependency from " + fromActivityId + " to " + toActivityId , e);
        }
    }
	
    private void checkCircularDependency(int fromActivityId, int toActivityId, String string) {
		if(checkLevel(fromActivityId, toActivityId) == true) {
			throw new ServiceFunctionalityException(string);
		}
	}
    
    private boolean checkLevel(int toAdd, int activityId) {
    	
    	List<Integer> links = getDependencies(toAdd);
    	ArrayList<Boolean> linkChecks = new ArrayList<Boolean>();
    	for(int x : links) {
    		if(x == toAdd || x == activityId) {
    			return true;
    		}
    		linkChecks.add(checkLevel(x, toAdd));
    	}

    	if(linkChecks.contains(new Boolean(true))) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
	@Override
    public List<Integer> getDependencies(int toActivityId) {

        List<Activitylinks> links;

        try {
            links = activitylinksDao.fetchByToActivityId(toActivityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get dependencies of activity " + toActivityId , e);
        }

        return links.stream()
                .map( Activitylinks::getFromActivityId )
                .collect( Collectors.toList() );
    }

    @Override
    public List<Integer> getDependents(int fromActivityId) {

        List<Activitylinks> links;

        try {
            links = activitylinksDao.fetchByFromActivityId(fromActivityId);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get dependents of activity " + fromActivityId , e);
        }

        return links.stream()
                .map( Activitylinks::getToActivityId )
                .collect( Collectors.toList() );
    }

    private void checkProjectNotCompleted( String errMsg ) {   	
        if ( getProject().getCompleted() ) {
            throw new IllegalStateException( errMsg );
        }
    }

    @Override
    public void addUserToActivity(int activityId, Users user) {
    	
    	checkUserInProject(user.getId(), "User is not in project activity belongs to");
    	
	    try {
	    	userActivitiesDao.insert(new Useractivities ( null, activityId, user.getId()));
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add a new user to activity", e);
	    }
    }
    
    private void checkUserInProject( int memberId, String errMsg ) {   
        if ( getProjectMember(memberId) == null ) {
            throw new IllegalStateException( errMsg );
        }
    }

    @Override
    public void deleteUserFromActivity(int activityId, Users user) {
	    try {
	    	create.deleteFrom(Tables.USERACTIVITIES)
	    	.where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))
	    	.and(Tables.USERACTIVITIES.USER_ID.equal(user.getId()))
	    	.execute();
	    } catch (DataAccessException e) {
	        throw new ServiceFunctionalityException("failed to add delete user from activity", e);
	    }
    }
    
    @Override
    public List<Users> getAssigneesForActivity(int activityId)
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.USERACTIVITIES).on(Tables.USERACTIVITIES.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for activity " + activityId, e);
        }
    }

	@Override
	public List<Users> getUnassignedMembersForActivity(int activityId) {
        try {
        	return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(create.select(Tables.ACTIVITY.PROJECT_ID)
                    		.from(Tables.ACTIVITY)
                    		.where(Tables.ACTIVITY.ID.equal(activityId))))
                    .and(Tables.PROJECTMEMBERS.USER_ID.notIn(create.select(Tables.PROJECTMEMBERS.USER_ID)
                    		.from(Tables.PROJECTMEMBERS)
                    		.join(Tables.USERACTIVITIES)
                    		.on(Tables.USERACTIVITIES.USER_ID.equal(Tables.PROJECTMEMBERS.USER_ID))
                    		.where(Tables.USERACTIVITIES.ACTIVITY_ID.equal(activityId))))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for activity " + activityId, e);
        }
	}
	
	@Override
    public Users getProjectMember(int memberId)
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(this.projectId))
                    .and(Tables.PROJECTMEMBERS.USER_ID.equal(memberId))
                    .fetchOneInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project member "+memberId+" for project", e);
        }
    }
	
	@Override
    public List<Users> getProjectMembers()
    {
        try {
            return create.select(Tables.USERS.fields())
                    .from(Tables.USERS)
                    .join(Tables.PROJECTMEMBERS).on(Tables.PROJECTMEMBERS.USER_ID.equal(Tables.USERS.ID))
                    .where(Tables.PROJECTMEMBERS.PROJECT_ID.equal(this.projectId))
                    .fetchInto(Users.class);
        } catch (DataAccessException e) {
            throw new ServiceFunctionalityException("failed to get project members for project", e);
        }
    }
	
	@Override
	public List<Integer> calculateNumberOfStartingNodes(List<Integer> startingNodes, int activityId) {
		List<Integer> acts = this.getDependencies(activityId);
		
		if(acts.size() > 0) {
			for(Integer aId : acts) {
				List<Integer> nodes = calculateNumberOfStartingNodes(startingNodes, aId);
				for(Integer newId : nodes) {
					if(!startingNodes.contains(newId)) {
						startingNodes.add(newId);
					}
				}
			}
			
		}
		else {
			if(!startingNodes.contains(activityId)) {
				startingNodes.add(activityId);
			}
		}
		
		return startingNodes;
	}
	
	@Override
	public List<Integer> calculateNumberOfEndingNodes(List<Integer> endingNodes, int activityId) {
		List<Integer> acts = this.getDependents(activityId);
		
		if(acts.size() > 0) {
			for(Integer aId : acts) {
				List<Integer> nodes = calculateNumberOfEndingNodes(endingNodes, aId);
				for(Integer newId : nodes) {
					if(!endingNodes.contains(newId)) {
						endingNodes.add(newId);
					}
				}
			}
			
		}
		else {
			if(!endingNodes.contains(activityId)) {
				endingNodes.add(activityId);
			}
		}
		
		return endingNodes;
	}

	@Override
	public List<Integer> calculateSizeOfChain(List<Integer> nodes, int activityId) {
		if(!nodes.contains(activityId)) { //always try to add current activityId
			nodes.add(activityId);
		}
		
		List<Integer> actsBackward = this.getDependencies(activityId);
		List<Integer> actsForward = this.getDependents(activityId);
		
		if(actsBackward.size() > 0) { //middle/end
			for(Integer aId : actsBackward) {
				List<Integer> newNodes = calculateAllBackward(nodes, aId);
				for(Integer newId : newNodes) {
					if(!nodes.contains(newId)) {
						nodes.add(newId);
					}
				}
			}
		}
		
		if(actsForward.size() > 0) { //middle/end
			for(Integer aId : actsForward) {
				List<Integer> newNodes = calculateAllForward(nodes, aId);
				for(Integer newId : newNodes) {
					if(!nodes.contains(newId)) {
						nodes.add(newId);
					}
				}
			}
		}
		return nodes;
	}
	
	private List<Integer> calculateAllForward(List<Integer> activities, int activityId) {
		if(!activities.contains(activityId)) {
			activities.add(activityId);
		}
		
		List<Integer> acts = this.getDependents(activityId);
		if(acts.size() > 0) {
			for(Integer aId : acts) {
				if(!activities.contains(aId)) {
					activities.add(aId);
				}
				List<Integer> nodes = calculateAllForward(activities, aId);
				for(Integer newId : nodes) {
					if(!activities.contains(newId)) {
						activities.add(newId);
					}
				}
			}
		}
		
		return activities;
	}
	
	private List<Integer> calculateAllBackward(List<Integer> activities, int activityId) {
		if(!activities.contains(activityId)) {
			activities.add(activityId);
		}
		
		List<Integer> acts = this.getDependencies(activityId);
		if(acts.size() > 0) {
			for(Integer aId : acts) {
				if(!activities.contains(aId)) {
					activities.add(aId);
				}
				List<Integer> nodes = calculateAllBackward(activities, aId);
				for(Integer newId : nodes) {
					if(!activities.contains(newId)) {
						activities.add(newId);
					}
				}
			}
		}
		
		return activities;
	}
	
	@Override
	public List<Integer> calculateAllParamsOfChain(int startActivityId, int endActivityId) {
		Activity startActivity = this.getActivity(startActivityId);
		List<Integer> activities = calculateNextForward(startActivity);
		
		Activity lastActivity = this.getActivity(endActivityId);
		lastActivity.setLatestFinish(lastActivity.getEarliestFinish());
		lastActivity.setLatestStart(lastActivity.getEarliestStart());
		lastActivity.setFloat(0);
		lastActivity.setMaxDuration(lastActivity.getDuration());
		
		this.updateActivity(lastActivity);
		
//		System.out.println("Activity "+lastActivity.getLabel());
//		System.out.println("    Earliest Start - "+lastActivity.getEarliestStart());
//		System.out.println("    Earliest Finish - "+lastActivity.getEarliestFinish());
//		System.out.println("    Latest Start - "+lastActivity.getLatestStart());
//		System.out.println("    Latest Finish - "+lastActivity.getLatestFinish());
//		System.out.println("    Float - "+lastActivity.getFloat());
//		System.out.println("    Duration - "+lastActivity.getDuration());
//		System.out.println("    Max Duration - "+lastActivity.getMaxDuration());
		
		calculatePrevBackward(lastActivity);
		
		return activities;
	}
	
	private void calculatePrevBackward(Activity a) {
		Integer lastActivityLatestStart = -1;
		for(int i : this.getDependents(a.getId())) {
			if(this.getActivity(i).getLatestStart() != null) {
				if(lastActivityLatestStart == -1 || this.getActivity(i).getLatestStart() < lastActivityLatestStart) {
					lastActivityLatestStart = this.getActivity(i).getLatestStart();
				}
			}
		}
		
		if(lastActivityLatestStart != -1) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				Calendar cal = Calendar.getInstance();
				
				a.setLatestFinish(lastActivityLatestStart);
				
				cal.setTime(formatter.parse(a.getLatestFinish().toString()));
				cal.add(Calendar.DATE, -1 * a.getDuration());
				
				Integer currentActivityLatestStart = Integer.parseInt(formatter.format(cal.getTime()));
				
				a.setLatestStart(currentActivityLatestStart);
				
				Date ES = formatter.parse(a.getEarliestStart().toString());
				Date LS = formatter.parse(a.getLatestStart().toString());
				int floatVal = (int) (LS.getTime() - ES.getTime()) / (1000 * 60 * 60 * 24);
				a.setFloat(floatVal);
				
				Date LF = formatter.parse(a.getLatestFinish().toString());
				int maxDuration = (int) (LF.getTime() - ES.getTime()) / (1000 * 60 * 60 * 24);
				a.setMaxDuration(maxDuration);
				this.updateActivity(a);
						
//				System.out.println("Activity "+a.getLabel());
//				System.out.println("    Earliest Start - "+a.getEarliestStart());
//				System.out.println("    Earliest Finish - "+a.getEarliestFinish());
//				System.out.println("    Latest Start - "+a.getLatestStart());
//				System.out.println("    Latest Finish - "+a.getLatestFinish());
//				System.out.println("    Float - "+a.getFloat());
//				System.out.println("    Duration - "+a.getDuration());
//				System.out.println("    Max Duration - "+a.getMaxDuration());
			} catch (ParseException e) {}
		}
		
		for(int i : this.getDependencies(a.getId())) {
			calculatePrevBackward(this.getActivity(i));
		}	
	}

	private List<Integer> calculateNextForward(Activity a) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		List<Integer> activities = new ArrayList<Integer>();
		activities.add(a.getId());
		
		try {
			Date startDate = formatter.parse(a.getEarliestStart().toString());
			Date endDate = formatter.parse(a.getEarliestFinish().toString());
			int duration = (int) (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
			a.setDuration(duration);
			this.updateActivity(a);
			
			for(int i : this.getDependents(a.getId())) {
				List<Integer> newActs = calculateNextForward(this.getActivity(i));
				for(Integer na : newActs) {
					if(!activities.contains(na)) {
						activities.add(na);
					}
				}
			}
						
		} catch (ParseException e) {}
		
		return activities;
	}
}
