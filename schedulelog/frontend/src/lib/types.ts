export interface Activity {
    content: string,
    subjects: Subject[],
    startTime: string,
    endTime: string,
}
  
export interface Subject {
    code: string
    name: string
}