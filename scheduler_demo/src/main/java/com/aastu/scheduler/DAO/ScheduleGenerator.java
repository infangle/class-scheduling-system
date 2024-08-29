package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Classroom;
import com.aastu.scheduler.models.Course;
import com.aastu.scheduler.models.Instructor;
import com.aastu.scheduler.models.Schedule;

import java.time.LocalTime;
import java.util.*;

//public class ScheduleGenerator {
//    private final List<Course> courses;
//    private final List<Instructor> instructors;
//    private final List<Classroom> classrooms;
//    private final ScheduleDao scheduleDao;
//
//    public ScheduleGenerator(List<Course> courses, List<Instructor> instructors, List<Classroom> classrooms, ScheduleDao scheduleDao) {
//        this.courses = courses;
//        this.instructors = instructors;
//        this.classrooms = classrooms;
//        this.scheduleDao = scheduleDao;
//    }
//
//    private boolean isInstructorAvailable(Instructor instructor, String day, LocalTime timeSlot) {
//        List<Schedule> schedules = scheduleDao.getSchedulesForInstructor(instructor.getId(), day);
//        for (Schedule schedule : schedules) {
//            if (timeSlot.isBefore(schedule.getEndTime()) && timeSlot.plusHours(2).isAfter(schedule.getStartTime())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean isClassroomAvailable(Classroom classroom, String day, LocalTime timeSlot) {
//        List<Schedule> schedules = scheduleDao.getSchedulesForClassroom(classroom.getId(), day);
//        for (Schedule schedule : schedules) {
//            if (timeSlot.isBefore(schedule.getEndTime()) && timeSlot.plusHours(2).isAfter(schedule.getStartTime())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public List<Schedule> generateSchedules() {
//        // Generate schedules as before
//        List<Schedule> schedules = new ArrayList<>();
//        List<LocalTime> timeSlots = generateTimeSlots();
//        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
//
//        int scheduleId = 1;
//        for (Course course : courses) {
//            for (String day : daysOfWeek) {
//                for (LocalTime timeSlot : timeSlots) {
//                    Instructor instructor = findAvailableInstructor(course, day, timeSlot);
//                    Classroom classroom = findAvailableClassroom(day, timeSlot);
//
//                    if (instructor != null && classroom != null) {
//                        Schedule schedule = new Schedule(scheduleId++, course.getId(), instructor.getId(), classroom.getId(), timeSlot, timeSlot.plusHours(1), day);
//                        schedules.add(schedule);
//                    }
//                }
//            }
//        }
//
//        return schedules;
//    }
//
//    private List<LocalTime> generateTimeSlots() {
//        List<LocalTime> timeSlots = new ArrayList<>();
//        timeSlots.add(LocalTime.of(8, 30));
//        timeSlots.add(LocalTime.of(10, 30));
//        timeSlots.add(LocalTime.of(12, 30));
//        timeSlots.add(LocalTime.of(14, 30));
//
//        return timeSlots;
//    }
//
//    private Instructor findAvailableInstructor(Course course, String day, LocalTime timeSlot) {
//        List<Instructor> instructors = new InstructorDao().getInstructorsByCourseId(course.getId());
//        for (Instructor instructor : instructors) {
//
//            if (isInstructorAvailable(instructor, day, timeSlot)) {
//                return instructor;
//            }
//        }
//        return null;
//    }
//
//    private Classroom findAvailableClassroom(String day, LocalTime timeSlot) {
//        for (Classroom classroom : classrooms) {
//            if (isClassroomAvailable(classroom, day, timeSlot)) {
//                return classroom;
//            }
//        }
//        return null;
//    }
//}


public class ScheduleGenerator {
    private final CourseDao courseDao;
    private final InstructorDao instructorDao;
    private final ClassroomDao classroomDao;
    private final ScheduleDao scheduleDao;

    public ScheduleGenerator(CourseDao courseDao, InstructorDao instructorDao, ClassroomDao classroomDao, ScheduleDao scheduleDao) {
        this.courseDao = courseDao;
        this.instructorDao = instructorDao;
        this.classroomDao = classroomDao;
        this.scheduleDao = scheduleDao;
    }

    public List<Schedule> generateSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        List<Course> courses = courseDao.getAllCourses();
        List<Instructor> instructors = instructorDao.getAllInstructors();
        List<Classroom> classrooms = classroomDao.getAllClassrooms();
        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        List<Integer> years = Arrays.asList(1,2,3,4,5);

        LocalTime startTime = LocalTime.of(8, 30); // Start at 8:30 AM
        LocalTime endTime = LocalTime.of(16, 30); // End at 10:30 AM

        Set<Integer> assignedInstructors = new HashSet<>();

        Random random = new Random();

      int scheduleId = 1;
      for(int year: years) {
          for (String day : daysOfWeek) {
              for (Course course : courses) {

                  LocalTime timeSlot = startTime;

                  while (timeSlot.plusHours(2).isBefore(endTime)) { // Assuming each class is 2 hours
                      System.out.println("--------------------");
                      LocalTime classEndTime = timeSlot.plusHours(2);

                      Instructor instructorrr = instructors.get(random.nextInt(instructors.size()));
                      if (instructorDao.isInstructorTeachesCourse(instructorrr.getId(), course.getId())) {
                          System.out.println(instructorrr.getId() + " " + instructorrr.getName() + " " + instructorrr.getDepartment() + " " + instructorrr.getCourseId() + " " + instructorrr.getEmail() + " " + instructorrr.getCourse() + " " + instructorrr.getCourseId());
//                                if (instructorDao.isInstructorTeachesCourse(instructorrr.getId(), course.getId())) {
                          // Skip if the instructor is already assigned to a course
                          if (assignedInstructors.contains(instructorrr.getId())) {
                              continue;
                          }

                          Classroom classroomm = classrooms.get(random.nextInt(classrooms.size()));

                          if (isInstructorAvailable(instructorrr, day, timeSlot, classEndTime) &&
                                  isClassroomAvailable(classroomm, day, timeSlot, classEndTime) && scheduleDao.countSchedulesForCourseDay(classroomm.getId(), timeSlot, classEndTime, day) == 0) {
                              System.out.println("####################33");
                              Schedule schedule = new Schedule(scheduleId++, course.getId(), instructorrr.getId(), classroomm.getId(), timeSlot, classEndTime, day,year);
                              schedules.add(schedule);
                              scheduleDao.addSchedule(schedule);
                              assignedInstructors.add(instructorrr.getId());
                              break;
                          }
                          timeSlot = timeSlot.plusHours(1); // Move to the next hour
                      }
                  }
              }


          }
      }
        return schedules;
    }

    private boolean isInstructorAvailable(Instructor instructor, String day, LocalTime startTime, LocalTime endTime) {
        List<Schedule> schedules = scheduleDao.getSchedulesForInstructor(instructor.getId(), day);
        for (Schedule schedule : schedules) {
            // Check if the proposed time slot overlaps with any existing schedule
            if (startTime.isBefore(schedule.getEndTime()) && endTime.isAfter(schedule.getStartTime())) {
                return false; // Overlap found
            }
        }
        return true; // No overlap
    }

    private boolean isClassroomAvailable(Classroom classroom, String day, LocalTime startTime, LocalTime endTime) {
        List<Schedule> schedules = scheduleDao.getSchedulesForClassroom(classroom.getId(), day);
        for (Schedule schedule : schedules) {
            // Check if the proposed time slot overlaps with any existing schedule
            if (startTime.isBefore(schedule.getEndTime()) && endTime.isAfter(schedule.getStartTime())) {
                return false; // Overlap found
            }
        }
        return true; // No overlap
    }

}
