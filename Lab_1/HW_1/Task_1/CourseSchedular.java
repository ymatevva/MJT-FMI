
package HW_1.Task_1;

public class CourseSchedular{

  
    
    private static void swapCourses(int[] courseOne, int[] courseTwo){
        int tmp = courseOne[0];
        courseOne[0] = courseTwo[0];
        courseTwo[0] = tmp;

        tmp = courseOne[1];
        courseOne[1] = courseTwo[1];
        courseTwo[1] = tmp;

    }
    
    private static void sortCourses(int[][] array){

        int end = array.length-1;
        for (int i = 0; i < array.length; i++) {
            
            int lastSwappedInd = 0;
            for (int j = 0; j < end; j++) {
                if(array[j][1] > array[j+1][1]){
                    swapCourses(array[j],array[j+1]);
                    lastSwappedInd = j;
                }
            }
            if(lastSwappedInd == 0){
               return;
            }
            end = lastSwappedInd;
        }
    }

    public static int maxNonOverlapingCourses(int[][] array){

        int maxCount = 0;
        sortCourses(array);

        for (int i = 0; i < array.length; i++) {
            
          
            int currCount = 1;
            int startHour =  array[i][0];
            int endHour = array[i][1];

            for (int j = i+1; j < array.length; j++) {
                if(array[j][0] >= endHour){
                    startHour = array[j][0];
                    endHour = array[j][1];
                    currCount++;
                }
            
            }

            if(currCount > maxCount){
                maxCount = currCount;
            }   
        }

        return maxCount;
    }

}