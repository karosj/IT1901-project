import styles from './WriteActivity.module.css';
import dummySubjects from '../../lib/dummy/subjects.json'
import { Subject } from '../../lib/types';
import { useEffect, useState } from 'react';

export default function WriteActivity() {

    const [subjects, setSubjects] = useState<Subject[] | null>(null)

    // Since we will replace dummy with real content, we use a effect hook to fetch the data
    useEffect(() => {
        setSubjects(dummySubjects)
    }, [dummySubjects])

    return <div className={styles.card}>
        <h3>Write Activity</h3>
        <form action="">
            <textarea placeholder='Write content' />
            <select name="subjects" id="subjects">
                <option value="default">No selected subject</option>
                {subjects?.map((subject: Subject) => {
                    return <option value={subject.code}>{subject.name}</option>
                })}
            </select>
            <button type='submit'>Submit</button>
        </form>
    </div>
}