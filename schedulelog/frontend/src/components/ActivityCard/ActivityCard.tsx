import { Activity } from "../../lib/types";
import styles from "./ActivityCard.module.css"

export default function ActivityCard({ content, startTime }: Activity) {

    return <div className={styles.card}>
        <h3>{new Date(startTime).toLocaleString()}</h3>
        <p>{content}</p>
    </div>
}