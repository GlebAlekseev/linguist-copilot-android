import nltk

nltk.download('averaged_perceptron_tagger')

def is_passive(sentence):
    be_forms = {'am', 'is', 'are', 'been', 'was', 'were', 'be', 'being'}  # all forms of "be"
    aux_verbs = {'do', 'did', 'does', 'have', 'has', 'had'}  # auxiliary verbs
    words = nltk.word_tokenize(sentence)
    tokens = nltk.pos_tag(words)
    tags = [tag for word, tag in tokens]

    if tags.count('VBN') == 0:  # No past participle, no passive voice.
        return False
    elif tags.count('VBN') == 1 and 'been' in words:  # One past participle "been", still no passive voice.
        return False
    else:
        pp_indices = [i for i, tag in enumerate(tags) if tag == 'VBN' and words[i] != 'been']  # Gather all the past participles that are not "been".
        for end in pp_indices:
            chunk = tags[:end]
            start = 0
            for i in range(len(chunk), 0, -1):
                last = chunk.pop()
                if last == 'NN' or last == 'PRP':
                    start = i  # Get the chunk between PP and the previous NN or PRP (which in most cases are subjects)
                    break
            sent_chunk = words[start:end]
            tags_chunk = tags[start:end]
            verbs_pos = [i for i, tag in enumerate(tags_chunk) if tag.startswith('V')]  # Get all the verbs in between
            if verbs_pos:  # If there are no verbs in between, it's not passive
                for i in verbs_pos:
                    if sent_chunk[i].lower() not in be_forms and sent_chunk[i].lower() not in aux_verbs:  # Check if they are all forms of "be" or auxiliary verbs.
                        break
                else:
                    return True
    return False

def process(sentence):
    return is_passive(sentence)

if __name__ == '__main__':
    samples = '''
    The cake was baked by Mary.
    The letter has been sent by John.
    The problem will be solved by the team.
    The house was painted by the workers yesterday.
    The book has been read by many students.
    The door will be opened by the manager.
    The cake was delicious.
    Mary baked the cake.
    '''

    sents = nltk.sent_tokenize(samples)
    for sent in sents:
        print(sent + '--> %s' % is_passive(sent))
