import { useEffect, useState } from "react"
import dummyActivities from "../../lib/dummy/activities.json"
import  { Activity } from "../../lib/types"
import ActivityCard from "../ActivityCard/ActivityCard"
import styles from "./ActivitiesList.module.css"

export default function ActivitiesList() {

    const [activities, setActivities] = useState<Activity[] | null>(null)

    // Since we will replace dummy with real content, we use a effect hook to fetch the data
    useEffect(() => {
        const sorted = dummyActivities.sort((a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
        setActivities(sorted)
    }, [dummyActivities])

    return <div>
        <h2>Activities List</h2>
        <div className={styles.list}>
            {activities?.map((activity: Activity) => {
                return <ActivityCard {...activity} />
            })}
        </div>
    </div>
}