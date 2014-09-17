arr = ["394asdf", "ds1048", "-100sdjl"]
arr2 = ["thedsfkck", "ho10", "4094a"]
arr3 = ["psdlj3905", "3448hg009r", "osi12"]

#finds and sorts the array by the first number group that is found
#if the word contains no numbers, it is put first in the output
def funny_sort(arr)
  arr.sort do |a,b|
    first = /\d+/.match(a)
    second = /\d+/.match(b)
    firstInt = -1
    secondInt = -1
    if first != nil
      firstInt = first[0].to_i
    end
    if second != nil
      secondInt = second[0].to_i
    end
    a=0
    if firstInt>secondInt
      a=1
    elsif secondInt>firstInt
      a=-1
    end
    a
  end
end

puts funny_sort(arr)
puts funny_sort(arr2)
puts funny_sort(arr3)
