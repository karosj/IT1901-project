import { Activity } from "../../lib/types";
import styles from "./ActivityCard.module.css"

export default function ActivityCard({ content, startTime }: Activity) {

    return <div className={styles.card}>
        <h3>{content}</h3>
        <p>{new Date(startTime).toLocaleString()}</p>
    </div>
}